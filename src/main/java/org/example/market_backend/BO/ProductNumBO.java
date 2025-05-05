package org.example.market_backend.BO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;


@Setter
@Getter
@ToString(callSuper = true)
public class ProductNumBO implements Serializable {
    private static final long serialVersionUID = 1L;
    private  Integer publishNum;
    private BigDecimal publishAmount;
    private Integer saleNum;
    private BigDecimal saleAmount;
    private Integer purchaseNum;
    private BigDecimal purchaseAmount;
}
