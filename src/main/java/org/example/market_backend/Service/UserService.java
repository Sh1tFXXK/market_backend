package org.example.market_backend.Service;

import org.example.market_backend.BO.LoginBO;
import org.example.market_backend.Entity.User;
import org.example.market_backend.VO.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;




public interface UserService {
    void register(MultipartFile file, RegisterVO vo);

    void getVerifyCode(@Valid VerifyCodeVO vo);

    LoginBO login(@Valid LoginVO vo);

    void forgetPwd(@Valid ForgotVO vo);

    void updatePwd(@Valid UpdatePwdVO vo);

    void updateUserInfo(UserInfoVO vo);

    User checkToken(String token);

    void logout(String token);

    User findById(String toUserId);

    void verifyOldMobile(UpdateMobileVO vo);

    void bindNewMobile(UpdateMobileVO vo);
}
