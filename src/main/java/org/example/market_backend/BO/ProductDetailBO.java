package org.example.market_backend.BO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Setter
@Getter
@ToString(callSuper = true)
public class ProductDetailBO implements Serializable {
    private static final long serialVersionUID = 1L;
    private String publishUserId;
    private String publishUserName;
    private String publishUserAvatar;
    private Date publishTime;
    private Integer praiseStatus;
    private Integer productId;
    private String productImgs;
    private String productDesc;
    private BigDecimal productPrice;
    private Integer productTypeId;
    private String productTypeName;
    private Integer wantNum;
    private  String productAddress;
}
