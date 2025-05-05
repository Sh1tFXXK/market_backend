package org.example.market_backend.Entity;

import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 订单表
 * &#064;TableName  order
 */
@Setter
@Getter
@Table(name ="order")
public class Order {
    /**
     * 订单编号
     * -- GETTER --
     *  订单编号
     * -- SETTER --
     *  订单编号


     */
    @Id
    private String orderId;

    /**
     * 购买人id
     * -- GETTER --
     *  购买人id
     * -- SETTER --
     *  购买人id


     */
    private String buyingUserId;

    /**
     *
     * -- GETTER --
     *
     * -- SETTER --
     *
     *


     */
    private Integer productId;

    /**
     * 交易状态  1 已下架 2-已取消 3-已结算
     * -- GETTER --
     *  交易状态  1 已下架 2-已取消 3-已结算
     * -- SETTER --
     *  交易状态  1 已下架 2-已取消 3-已结算


     */
    private Integer tradeStatus;

    /**
     * 支付状态 1-待付款2-付款中 3-已付款 4-付款失败
     * -- GETTER --
     *  支付状态 1-待付款2-付款中 3-已付款 4-付款失败
     * -- SETTER --
     *  支付状态 1-待付款2-付款中 3-已付款 4-付款失败


     */
    private Integer payStatus;

    /**
     * 卖出状态 1-有效 2-删除
     * -- GETTER --
     *  卖出状态 1-有效 2-删除
     * -- SETTER --
     *  卖出状态 1-有效 2-删除


     */
    private Integer sellingStatus;

    /**
     * 订单金额
     * -- GETTER --
     *  订单金额
     * -- SETTER --
     *  订单金额


     */
    private BigDecimal orderAmount;

    /**
     * 支付金额
     * -- GETTER --
     *  支付金额
     * -- SETTER --
     *  支付金额


     */
    private BigDecimal payAmount;

    /**
     * 支付时间
     * -- GETTER --
     *  支付时间
     * -- SETTER --
     *  支付时间


     */
    private Date payTime;

    /**
     * 订单完成时间
     * -- GETTER --
     *  订单完成时间
     * -- SETTER --
     *  订单完成时间


     */
    private Date completionTime;

    /**
     * 第三方支付流水号
     * -- GETTER --
     *  第三方支付流水号
     * -- SETTER --
     *  第三方支付流水号


     */
    private String outTradeNo;

    /**
     * 订单创建时间
     * -- GETTER --
     *  订单创建时间
     * -- SETTER --
     *  订单创建时间


     */
    private Date createTime;

    /**
     * 修改时间
     * -- GETTER --
     *  修改时间
     * -- SETTER --
     *  修改时间


     */
    private Date updateTime;

    /**
     * 订单备注
     * -- GETTER --
     *  订单备注
     * -- SETTER --
     *  订单备注


     */
    private String remark;


    private Integer buyingStatus;
    private String consigneeAddress;
    private String consigneeName;
    private String consigneeMobile;
}