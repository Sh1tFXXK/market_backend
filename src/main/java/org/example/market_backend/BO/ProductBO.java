package org.example.market_backend.BO;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import java.io.Serializable;
import java.math.BigDecimal;


@Setter
@Getter
@ToString(callSuper = true)
public class ProductBO implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 商品id
     */
    private Integer id;
    /**
     * 商品图片
     */
    private String productImgs;
    /**
     * 商品描述
     */
    private String productDesc;
    /**
     * 商品价格
     */
    private BigDecimal productPrice;
    /**
     * 商品类型id
     */
    private Integer productTypeId;
    /**
     * 商品类型名称
     */
    private String productTypeName;
    /**
     * 商品发布人用户编号
     */
    private String publishUserId;
    /**
     * 商品发布人用户名称
     */
    private String publishUserName;
    /**
     * 商品发布时间
     */
    private Data createTime;
    /**
     * 商品用户头像
     */
    private String publishUserAvatar;
    /**
     * 购买人用户编号
     */
    private String buyingUserId;
    /**
     * 交易状态 ：1 未交易 ，2 交易中 ，3 交易完成， 4-交易失败
     */
    private  Integer tradeStatus;
    /**
     * 交易时间
     */
    private Data tradeTime;
    /**
     * 想要人数
     */
    private Integer wantNum;
    /**
     * 订单编号
     */
    private String orderId;
    /**
     * 商品地址
     */
    private String productAddress;


}
