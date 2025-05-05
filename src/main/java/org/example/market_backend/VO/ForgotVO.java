package org.example.market_backend.VO;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString(callSuper = true)
public class ForgotVO extends BaseVO{
    private static final long serialVersionUID = 1L;
    @NotBlank(message = "手机号不能为空")
    private String mobile;
    @NotBlank(message = "短信验证码不能为空")
    private String verifyCode;
    @NotBlank(message = "密码不能为空")
    private String password;
    @NotBlank(message = "确认密码不能为空")
    private String confirmPassword;
}
