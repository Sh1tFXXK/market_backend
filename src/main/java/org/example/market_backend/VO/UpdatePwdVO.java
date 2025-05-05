package org.example.market_backend.VO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import jakarta.validation.constraints.NotBlank;

@Setter
@Getter
@ToString(callSuper = true)
public class UpdatePwdVO extends BaseVO {
    private static final long serialVersionUID = 1L;
    @NotBlank(message = "手机号不能为空")
    private String mobile;
    @NotBlank(message = "旧密码不能为空")
    private String oldPassword;
    @NotBlank(message = "密码不能为空")
    private String password;
    @NotBlank(message = "确认密码不能为空")
    private String confirmPassword;
}
