package org.example.market_backend.VO;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString(callSuper = true)

public class UpdateMobileVO extends BaseVO{
    private  static final long serialVersionUID = 1L;

    private String mobile;
    private String verifyCode;
}
