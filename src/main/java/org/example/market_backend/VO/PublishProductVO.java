package org.example.market_backend.VO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

@Setter
@Getter
@ToString(callSuper = true)
public class PublishProductVO extends BaseVO{
    private static  final long serialVersionUID = 1L;
    private String productDesc;
    @NotNull(message = "商品价格不能为空")
    private BigDecimal productPrice;
    @NotNull(message = "商品类型不能为空")
    private Integer productTypeId;
    @NotNull(message = "商品地址不能为空")
    private String productAddress;

}
