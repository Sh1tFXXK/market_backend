package org.example.market_backend.VO;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString(callSuper = true)
public class UpdatePwdVO extends BaseVO{
    private static final long serialVersionUID = 1L;
    @NotBlank(message = "旧密码不能为空")
    private String oldPwd;
    @NotBlank(message = "新密码不能为空")
    private String newPwd;
    @NotBlank(message = "确认密码不能为空")
    private String confirmPwd;
}
