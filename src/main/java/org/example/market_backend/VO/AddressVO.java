package org.example.market_backend.VO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Setter
@Getter
@ToString(callSuper = true)
public class AddressVO extends BaseVO{
    private  static final long serialVersionUID = 1L;
    private  Integer id;
    @NotBlank(message = "收货人姓名不能为空")
    private  String consigneeName;
    @NotBlank(message = "收货人手机号不能为空")
    private  String consigneeMobile;
    @NotBlank(message = "省不能为空")
    private  String province;
    @NotBlank(message = "市不能为空")
    private  String city;
    @NotBlank(message = "区不能为空")
    private  String district;
    private   String street;
    @NotBlank(message = "详细地址不能为空")
    private  String addressDetail;
    @NotNull(message = "是否默认地址不能为空")
    private  Integer isDefaultAddress;
}
