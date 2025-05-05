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
        criteria.andEqualTo("nickname", vo.getNickName());
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

    /* 
    * 后续方法保持不变...
    */
}