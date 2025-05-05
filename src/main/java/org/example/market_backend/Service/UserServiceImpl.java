package org.example.market_backend.Service;

import org.example.market_backend.BO.LoginBO;
import org.example.market_backend.BO.TokenBO;
import org.example.market_backend.Constants.CodeEnum;
import org.example.market_backend.Constants.VerifyCodeConstants;
import org.example.market_backend.Constants.RedisConstants;
import org.example.market_backend.Entity.User;
import org.example.market_backend.Mapper.UserMapper;
import org.example.market_backend.Utils.AliSmsUtils;
import org.example.market_backend.Utils.OssUtil;
import org.example.market_backend.Utils.RedisUtils;
import org.example.market_backend.Utils.TokenUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson2.util.DateUtils;
import com.aliyun.oss.OSS;

import jakarta.validation.constraints.NotBlank;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.example.market_backend.VO.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;
import tk.mybatis.mapper.entity.Example;

import jakarta.validation.Valid;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import org.example.market_backend.Exception.BusinessException;

@Service
public class UserServiceImpl implements UserService {
    // 声明日志对象
    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
    
    @Autowired
    private UserMapper userMapper;
    
    private Example.Criteria criteria;
    
    @Autowired
    private RedisUtils RedisUtils;
    
    private DigestUtils MD5Utils = new DigestUtils();
    private UUID uuid;

    /**
     * 注册接口
     *
     * @param file-
     * @param vo
     */
    @Override
    public void register(MultipartFile file, RegisterVO vo){
        String key =String.format(RedisConstants.REGISTER_VERIFY_CODE, vo.getMobile());
        //获取缓存中的验证码
        String code =RedisUtils.get(key);
        Assert.notEmpty(Collections.singleton(code), "验证码已过期，请重新获取");
        Assert.isTrue(code != null && code.equals(vo.getVerifyCode()), "验证码不正确");
        //判断用户是否已经注册
        Example example = new Example(User.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("mobile", vo.getMobile());
        List<User> list =userMapper.selectByExample(example);
        Assert.isTrue(list.isEmpty(), "该手机号已经注册");
        example.clear();
        criteria=example.createCriteria();
        //判断昵称是否已经存在
        criteria.andEqualTo("nickname", vo.getUserName());
        List<User> userNames = userMapper.selectByExample(example);
        Assert.isTrue(userNames.isEmpty(), "该昵称已经存在");
        //上传头像，oss存储
        OSS ossClient = OssUtil.getOssClient();
        String bucketName = OssUtil.getBucketName();
        String diskName= "images/user" +DateUtils.toString(new Date());
        //根据当前时间戳及上传文件名生成最终的文件名
        String fileName = System.currentTimeMillis()+"_"+file.getOriginalFilename();
        boolean upload =false;
        try {
            upload= OssUtil.uploadInputStreamObject2Oss(ossClient,file.getInputStream(),fileName,bucketName,diskName);
        }catch (IOException e){
            Assert.isTrue(true, "上传失败");
        }
        //上传成功
        if (upload){
            //用户头像地址
            String userAvatar =OssUtil.getOssUrl()+"/"+diskName+"/"+fileName;
            //存储用户信息
            User user = new User();
            Date date = new Date();
            BeanUtils.copyProperties(vo, user);
            user.setUserId(UUID.randomUUID().toString());
            user.setPassword(Arrays.toString(MD5Utils.md5(vo.getPassword())));
            user.setCreateTime(date);
            user.setUpdateTime(date);
            user.setUserAvatar(userAvatar);
            userMapper.insertSelective(user);
            RedisUtils.delete(key);
        }else {
            log.info("上传失败");
        }
    }

    @Override
    public void getVerifyCode(@Valid VerifyCodeVO vo) {
        /**
         * 获取验证码
         * @param vo
         */
        String key;
        //注册验证码
        if(VerifyCodeConstants.REGISTER==vo.getType()){
            Example example = new Example(User.class);
            example.createCriteria().andEqualTo("mobile", vo.getMobile());
            User user = userMapper.selectOneByExample(example);
            Assert.isNull(user, "该手机号已经注册");
            key =String.format(RedisConstants.REGISTER_VERIFY_CODE, vo.getMobile());
        }else if (VerifyCodeConstants.OLD_MOBILE==vo.getType()){
            //校验旧手机号验证码
            User user=this.checkToken(vo.getToken());
            Assert.notNull(user, "手机号未注册");
            Assert.isTrue(user.getMobile().equals(vo.getMobile()), "手机号不匹配");
            key=String.format(RedisConstants.OLD_MOBILE_VERIFY_CODE, vo.getMobile());
        }else {
            Example example = new Example(User.class);
            example.createCriteria().andEqualTo("mobile", vo.getMobile());
            User user = userMapper.selectOneByExample(example);
            Assert.isNull(user, "该手机号已经注册");
            key=String.format(RedisConstants.NEW_MOBILE_VERIFY_CODE, vo.getMobile());
        }
        //生成6位数验证码
        int code = (int) ((Math.random() * 9 + 1) * 100000);
        //调用阿里云短信服务发送短信
        AliSmsUtils.getSms(vo.getMobile(), code);
        //将验证码存入redis中,设置5分钟过期
        RedisUtils.setEx(key, String.valueOf(code), 5, TimeUnit.MINUTES);
        log.info("验证码：{}发送成功", code);
    }

    @Override
    public LoginBO login(LoginVO vo) {
        //判断用户是否存在
        User old =checkUserExsits(vo.getMobile());
        Assert.notNull(old, "该用户不存在");
        Assert.isTrue(MD5Utils.md5(vo.getPassword()).equals(old.getPassword()), "密码错误");
        Assert.isTrue(old.getLoginStatus()==1, "该用户已被禁用");
        //已存在用户信息，封装修改实体
        User entity = new User();
        Date date = new Date();
        entity.setUserId(old.getUserId());
        entity.setLastLoginTime(date.getTime());
        entity.setLoginCounts(old.getLoginCounts()+1);
        entity.setLoginStatus(1);;
        entity.setUpdateTime(date);
        //生成token
        String token = TokenUtils.getToken(old.getUserId());
        //token失效时间点
        Long tokenExpired= System.currentTimeMillis() + RedisConstants.TOKEN_EXPIRE;
        entity.setToken(token);
        entity.setTokenExpired(tokenExpired);
        userMapper.updateByPrimaryKeySelective(entity);
        //封装token消息
        TokenBO tokenBO = new TokenBO();
        tokenBO.setUserId(old.getUserId());
        tokenBO.setMobile(old.getMobile());
        //删除旧token
        RedisUtils.delete(String.format(RedisConstants.TOKEN, old.getToken()));
        //将token存入redis中,并设置有效期
        RedisUtils.setEx(String.format(RedisConstants.TOKEN, token), String.valueOf(RedisConstants.TOKEN_EXPIRE), Long.parseLong(JSON.toJSONString(tokenBO)),TimeUnit.MINUTES);
        //将最新用户信息放入缓存并设置有效期
        BeanUtils.copyProperties(entity,old);
        RedisUtils.setEx(String.format(RedisConstants.USER_INFO, vo.getMobile()),RedisConstants.USER_INFO_EXPIRE, Long.parseLong(JSON.toJSONString(old)), TimeUnit.MINUTES);
        //封装返回客户端消息
        LoginBO bo = new LoginBO();
        BeanUtils.copyProperties(old, bo);
        bo.setLastLoginTime(date);
        bo.setToken(token);
        return bo;

    }

    private User checkUserExsits(@NotBlank(message = "手机号不能为空") String mobile) {
        Example example = new Example(User.class);
        example.createCriteria().andEqualTo("mobile", mobile);
        return userMapper.selectOneByExample(example);
    }

    /**
     *
     * web端用户忘记密码
     */
    @Override
    public void forgetPwd(@Valid ForgotVO vo) {
        Example example = new Example(User.class);
        example.createCriteria().andEqualTo("mobile", vo.getMobile());
        User user = userMapper.selectOneByExample(example);
        Assert.notNull(user, "该用户不存在");
        String key= String.format(RedisConstants.RETRIEVE_PWD_VERIFY_CODE, vo.getMobile());
        //获取缓存中的验证码
        String code = RedisUtils.get(key);
        Assert.notNull(code, "验证码已过期");
        Assert.isTrue(code.equals(vo.getToken()), "验证码错误");
        User retrieve = new User();
        Date date = new Date();
        retrieve.setUserId(user.getUserId());
        retrieve.setPassword(Arrays.toString(MD5Utils.md5(vo.getPassword())));
        retrieve.setUpdateTime(date);
        BeanUtils.copyProperties(retrieve, user);
        this.userMapper.updateUserInfo(retrieve,user);
        RedisUtils.delete(key);
    }

    @Override
    public void updatePwd(@Valid UpdatePwdVO vo) {
        //检验token是否有效
        User user = this.checkToken(vo.getToken());
        Assert.isTrue(user.getPassword().equals(MD5Utils.md5(vo.getOldPwd())), "旧密码错误");
        Assert.isTrue(!user.getPassword().equals(MD5Utils.md5(vo.getNewPwd())), "新密码不能与旧密码相同");
        //组装更新实体
        User update=new User();
        Date date = new Date();
        update.setUserId(user.getUserId());
        update.setPassword(Arrays.toString(MD5Utils.md5(vo.getNewPwd())));
        BeanUtils.copyProperties(update, user);
        //调用更新方法
        this.userMapper.updateUserInfo(update, user);
    }
    @Override
    public void updateUserInfo(UserInfoVO vo) {
        User user = this.checkToken(vo.getToken());
        //判断用户昵称是否重复
        Example example = new Example(User.class);
        example.createCriteria().andEqualTo("userName", vo.getUserName()).andNotEqualTo("userId", user.getUserId());
        User u = userMapper.selectOneByExample(example);
        Assert.isNull(u, "昵称重复");
        User update = new User();
        MultipartFile  file = vo.getUserAvatar();
        if (file != null) {
            // 上传图片
            OSS ossClient = OssUtil.getOssClient();
            String bucketName =OssUtil.getBucketName();
            String diskName= "image/usser/"+ DateUtils.toString(new Date());
            String  fileName= System.currentTimeMillis()+"_"+file.getOriginalFilename();
            try {
                OssUtil.uploadInputStreamObject2Oss(ossClient, file.getInputStream(), fileName, bucketName, diskName);
            }catch (Exception e){
                log.error("上传文件到阿里云OSS失败");
            }
            String userAvater=OssUtil.getOssUrl()+"/"+diskName+"/"+fileName;
            //更新用户头像
            if(StringUtils.isNotEmpty(userAvater)){
                update.setUserAvatar(userAvater);
            }
        }
        //更新用户昵称
        if(StringUtils.isNotEmpty(vo.getUserName())){
            update.setUserName(vo.getUserName());
        }
        update.setUserId(user.getUserId());
        BeanUtils.copyProperties(update, user);
        //修用户信息
        this.userMapper.updateUserInfo(update, user);
    }
    /**
     * 检验是否登录
     * @param token
     * @return
     */
    @Override
    public User checkToken(String token) {
        if(!StringUtils.isNotEmpty(token)){
            //无效登录专属异常999，客户端用于跳转登录页面判断
            throw new BusinessException(CodeEnum.TOKEN_FAILURE);
        }
        User user;
        //查询token是否有效
        TokenBO bo = new TokenBO();
        //从redis中获取token
        String tokenInfo = RedisUtils.get(String.format(RedisConstants.TOKEN, token));
        if (StringUtils.isNotEmpty(tokenInfo)) {
            bo= JSONObject.parseObject(tokenInfo, TokenBO.class);
            //查询用户消息
            String userInfo = RedisUtils.get(String.format(RedisConstants.USER_INFO, bo.getMobile()));
            if(!StringUtils.isNotEmpty(userInfo)){
                user=this.userMapper.findUserByToken(token);
                RedisUtils.setEx(String.format(RedisConstants.USER_INFO, bo.getMobile()),RedisConstants.USER_INFO_EXPIRE, Long.parseLong(JSON.toJSONString(user)),TimeUnit.MINUTES);
            }else{
                user=JSONObject.parseObject(userInfo, User.class);
            }
        }else {
            //redis无消息，在查询数据库
            user=this.userMapper.findUserByToken(token);
            bo.setMobile(user.getMobile());
            bo.setUserId(user.getUserId());
            String expirted = String.valueOf((int) ((user.getTokenExpired()-System.currentTimeMillis())/1000));
            RedisUtils.setEx(String.format(RedisConstants.TOKEN, token),expirted, Long.parseLong(JSON.toJSONString(bo)),TimeUnit.MINUTES);
        }
        Assert.notNull(user, "用户不存在");
        return user;
    }
    /**
     * 退出登录
     * @param token
     */
    @Override
    public void logout(String token) {
        String info = RedisUtils.get(String.format(RedisConstants.TOKEN, token));
        if(!StringUtils.isNotEmpty(info)){
            throw new BusinessException(CodeEnum.TOKEN_FAILURE);
        }
        TokenBO bo =JSONObject.parseObject(info, TokenBO.class);
        User user = new User();
        user.setUserId(bo.getUserId());
        user.setLoginStatus(2);
        user.setUpdateTime(new Date());
        userMapper.updateByPrimaryKeySelective(user);
        //删除redis中的token
        RedisUtils.delete(String.format(RedisConstants.TOKEN, token));
        //删除redis中的用户信息
        RedisUtils.delete(String.format(RedisConstants.USER_INFO, bo.getMobile()));
    }

    @Override
    public User findById(String toUserId) {
        return null;
    }

    @Override
    public void verifyOldMobile(UpdateMobileVO vo) {
        User user = this.checkToken(vo.getToken());
        Assert.isTrue(user.getMobile().equals(vo.getMobile()),  "手机号不一致");
        String code = RedisUtils.get(String.format(RedisConstants.OLD_MOBILE_VERIFY_CODE, vo.getMobile()));
        Assert.notEmpty(Collections.singleton(code), "验证码已过期");
        Assert.isTrue(code.equals(vo.getVerifyCode()), "验证码错误");
    }

    @Override
    public void bindNewMobile(UpdateMobileVO vo) {
        User user = this.checkToken(vo.getToken());
        Assert.isTrue(!user.getMobile().equals(vo.getMobile()),  "手机号不一致");
        String code = RedisUtils.get(String.format(RedisConstants.NEW_MOBILE_VERIFY_CODE, vo.getMobile()));
        Assert.notEmpty(Collections.singleton(code), "验证码已过期");
        Assert.isTrue(code.equals(vo.getVerifyCode()), "验证码错误");
        Example example = new Example(User.class);
        example.createCriteria().andEqualTo("mobile", vo.getMobile());
        List <User> userList = userMapper.selectByExample(example);
        Assert.isTrue(userList.isEmpty(), "手机号已被注册");
        User update = new User();
        update.setMobile(vo.getMobile());
        update.setUserId(user.getUserId());
        update.setUpdateTime(new Date());
        BeanUtils.copyProperties(update, user);
        this.userMapper.updateUserInfo(update, user);
        TokenBO tokenBO = new TokenBO();
        tokenBO.setMobile(vo.getMobile());
        tokenBO.setUserId(user.getUserId());
        int expirted = (int) ((user.getTokenExpired()-System.currentTimeMillis())/1000);
        RedisUtils.setEx(String.format(RedisConstants.TOKEN, user.getToken()), String.valueOf(tokenBO),expirted, TimeUnit.MINUTES);
    }
}
