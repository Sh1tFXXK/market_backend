package org.example.market_backend.VO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import jakarta.validation.constraints.NotBlank;

@Setter
@Getter
@ToString(callSuper = true)
public class VerifyCodeVO extends BaseVO{
    private static final long serialVersionUID = 1L;
    @NotBlank(message = "手机号不能为空")
    private String mobile;
    @NotBlank(message = "验证码类型不能为空")
    private Integer type;
}
