package org.example.market_backend.Entity;



import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

/**
* 商品表
* @TableName product
*/
@Setter
@Getter
@Table(name = "product")
public class Product {

    /**
    * 商品编号
    */
    @Id
    @Column(name = "id")
    private Integer id;
    /**
    * 商品图片，多个图片地址用英文逗号隔开
    */
    @Column(name = "product_imgs")
    private String productImgs;
    /**
    * 商品描述
    */
    @Column(name = "product_desc")
    private String productDesc;
    /**
    * 商品价格
    */
    @Column(name = "product_price")
    private BigDecimal productPrice;
    /**
    * 商品类别
    */
    @Column(name = "product_type_id")
    private Integer productTypeId;
    /**
    * 发布人
    */
    @Column(name = "publish_user_id")
    private String publishUserId;
    /**
    * 商品发布时的地理位置
    */
    @Column(name = "product_address")
    private String productAddress;
    /**
    * 创建时间
    */
    @Column(name = "create_time")
    private Date createTime;
    /**
    * 修改时间
    */
    @Column(name = "update_time")
    private Date updateTime;
    /**
    * 备注
    */
    @Column(name = "remark")
    private String remark;

}
