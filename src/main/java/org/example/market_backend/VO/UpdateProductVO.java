package org.example.market_backend.VO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;


@Setter
@Getter
@ToString(callSuper = true)
public class UpdateProductVO extends BaseVO{
    private static final long serialVersionUID = 1L;
    private String productDesc;
    private BigDecimal productPrice;
    private Integer productTypeId;
    @NotNull(message = "商品编号不能为空")
    private Integer productId;
    private String productAddress;
    /**
     * 旧商品图片
     */
    private String[] oldImgs;
}
