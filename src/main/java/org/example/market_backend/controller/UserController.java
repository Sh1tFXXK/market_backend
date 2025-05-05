package org.example.market_backend.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.market_backend.BO.ResponseBO;
import org.example.market_backend.Constants.VerifyCodeConstants;
import org.example.market_backend.Entity.User;
import org.example.market_backend.Service.UserService;
import org.example.market_backend.Utils.RedisUtils;
import org.example.market_backend.Utils.TokenUtils;
import org.example.market_backend.VO.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import jakarta.validation.Valid;
import java.util.Collections;

/**
 * 注册接口
 * @return
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private RedisUtils redisUtils;

    @RequestMapping(value = "/register", method = RequestMethod.POST,produces= MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseBO register(@RequestParam("userAvatar") MultipartFile userAvatar, @Valid RegisterVO vo) {
        log.info("开始注册");
        Assert.notNull(userAvatar, "用户头像不能为空");
        Assert.isTrue(vo.isMobile(vo.getMobile()), "手机号码格式不正确");
        Assert.isTrue(!vo.getPassword().equals(vo.getConfirmPassword()), "两次输入的密码不一致");
        // 调用业务逻辑层，注册服务
        userService.register(userAvatar, vo);
        return ResponseBO.success();
    }

    /**
     * 获取验证码接口
     * @param vo
     * @return
     */
    @RequestMapping(value = "getVerifyCode", method = RequestMethod.POST,produces= MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseBO getVerifyCode(@Valid @RequestBody VerifyCodeVO vo) {
        log.info("开始获取验证码");
        Assert.isTrue(vo.isMobile(vo.getMobile()), "手机号码格式不正确");
        Assert.isTrue(VerifyCodeConstants.REGISTER==vo.getType() || VerifyCodeConstants.RETRIEVE_PWD==vo.getType()
        ||VerifyCodeConstants.OLD_MOBILE==vo.getType()
        ||VerifyCodeConstants.NEW_MOBILE==vo.getType(), "验证码类型错误");
        if(VerifyCodeConstants.OLD_MOBILE==vo.getType()){
            //校验token是否为空
            String token=checkToken();
            vo.setToken(token);
        }
        userService.getVerifyCode(vo);
        return ResponseBO.success();
    }
    /**
     * 登录接口
     * @param vo
     * @return
     */
    @RequestMapping(value = "login", method = RequestMethod.POST,produces= MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseBO login(@Valid @RequestBody LoginVO vo) {
        Assert.isTrue(vo.isMobile(vo.getMobile()), "手机号码格式不正确");
        userService.login(vo);
        return ResponseBO.success();
    }
    /**
     * 忘记密码
     * @param vo
     * @return
     */
    @RequestMapping(value = "forgetPwd", method = RequestMethod.POST,produces= MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseBO forgetPwd(@Valid @RequestBody ForgotVO vo) {
        Assert.isTrue(vo.isMobile(vo.getMobile()), "手机号码格式不正确");
        Assert.isTrue(vo.getPassword().equals(vo.getConfirmPassword()), "两次输入的密码不一致");
        userService.forgetPwd(vo);
        return ResponseBO.success();
    }

    /**
     * 修改密码
     * @param vo
     * @return
     */
    @RequestMapping(value="updatePwd",method=RequestMethod.POST,produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseBO updatePwd(@Valid @RequestBody UpdatePwdVO vo){
        Assert.isTrue(vo.isMobile(vo.getMobile()), "手机号码格式不正确");
        Assert.isTrue(vo.getPassword().equals(vo.getConfirmPassword()), "两次输入的密码不一致");
        userService.updatePwd(vo);
        return ResponseBO.success();
    }
    /**
     * 修改用户信息
     * @param vo
     * @return
     */
    @RequestMapping(value="updateUserInfo",method=RequestMethod.POST,produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseBO updateUserInfo( UserInfoVO vo){
        //检验token是否为空
        String token=checkToken();
        boolean flag = (vo.getUserName() == null || vo.getUserName().trim().isEmpty()) && vo.getUserAvatar() == null;
        Assert.isTrue(flag,"用户信息不能为空");
        vo.setToken(token);
        //调用service层修改用户信息
        userService.updateUserInfo(vo);
        return ResponseBO.success();
    }
    /**
     * 判断登录是否有效
     * @return
     */
    @RequestMapping(value = "checkLoginValid" ,method = RequestMethod.POST)
    public ResponseBO checkLoginValid(){
        //检验token是否为空
        String token=checkToken();
        //调用service层判断登录是否有效
        userService.checkToken(token);
        return ResponseBO.success();
    }
    /**
     * 退出登录
     * @return
     */
    @RequestMapping(value = "logout" ,method = RequestMethod.POST)
    public ResponseBO logout(){
        //检验token是否为空
        String token=checkToken();
        //调用service层退出登录
        userService.logout(token);
        return ResponseBO.success();
    }
    private String checkToken(){
        // 从请求头获取token
        String token = TokenUtils.getRequestToken();
        Assert.notNull(token, "用户未登录");
        // 通过userService验证token
        User user = userService.checkToken(token);
        Assert.notNull(user, "用户未登录或登录已过期");
        return token;
    }
    /**
     * 校验旧手机号
     */
    @RequestMapping(value = "verifyOldMobile", method = RequestMethod.POST)
    public ResponseBO verifyOldMobile( @RequestBody UpdateMobileVO vo) {
        String token=checkToken();
        Assert.notEmpty(Collections.singleton(vo.getMobile()), "手机号不能为空");
        Assert.notEmpty(Collections.singleton(vo.getVerifyCode()), "验证码不能为空");
        vo.setToken(token);
        userService.verifyOldMobile(vo);
        return ResponseBO.success();
    }
    /**
     * 绑定新手机号
     */
    @RequestMapping(value = "bindNewMobile", method = RequestMethod.POST)
    public ResponseBO bindNewMobile( @RequestBody UpdateMobileVO vo) {
        String token=checkToken();
        Assert.notEmpty(Collections.singleton(vo.getMobile()), "手机号不能为空");
        Assert.notEmpty(Collections.singleton(vo.getVerifyCode()), "验证码不能为空");
        vo.setToken(token);
        userService.bindNewMobile(vo);
        return ResponseBO.success();
    }


}
