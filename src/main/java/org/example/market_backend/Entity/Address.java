package org.example.market_backend.Entity;

import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 地址表
 * @TableName address
 */
@Setter
@Getter
@Table(name ="address")
public class Address {
    /**
     * 自增主键
     * -- GETTER --
     *  自增主键
     * -- SETTER --
     *  自增主键


     */
    @Id
    private Integer id;

    /**
     * 用户编号
     * -- GETTER --
     *  用户编号
     * -- SETTER --
     *  用户编号


     */

    private String userId;

    /**
     * 详细地址
     */
    private String addressDetail;

    /**
     * 默认地址状态
     */
    private Integer isDefaultAddress;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 状态
     * -- SETTER --
     *  状态

     */
    @Setter
    private Integer status;

    /**
     * 签收昵称
     */
    private String consigneeName;

    /**
     * 签收手机号
     */
    private String consigneeMobile;

    /**
     * 省份
     */
    private String province;

    /**
     * 城市
     */
    private String city;

    /**
     * 区
     */
    private String district;
    private String detail;
    private String street;


}