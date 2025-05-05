package org.example.market_backend.BO;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@ToString
@Setter
@Getter
public class OrderBO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String orderId;
    private BigDecimal orderAmount;
    private Integer payStatus;
    private Integer tradeStatus;
    private String  buyerUserName;
    private Date createTime;
    private  String productDesc;
    private String productImgs;
    private String consigneeMobile;
    private  String consigneeName;
    private String consigneeAddress;
    private Long expiredTime;
}
