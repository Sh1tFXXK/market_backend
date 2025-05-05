package org.example.market_backend.VO;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString(callSuper = true)
public class OrderVO extends BaseVO{
    private static final long serialVersionUID = 1L;
    private Integer productId;
    private Integer addressId;
}
