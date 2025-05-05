package org.example.market_backend.VO;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Setter
@Getter
@ToString(callSuper = true)
public class RegisterVO extends BaseVO implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 昵称
     */
    @NotBlank(message = "昵称不能为空")
    private String nickName;
    /**
     * 手机号
     */
    @NotBlank(message = "手机号不能为空")
    private String mobile;

    /**
     * 验证码
     */
    @NotBlank(message = "验证码不能为空")
    private String verifyCode;
    /**
     * 密码
     */
    @Setter
    @Getter
    @NotBlank(message = "密码不能为空")
    private String password;
    /**
     * 确认密码
     */
    @NotBlank(message = "确认密码不能为空")
    private String confirmPwd;



}
