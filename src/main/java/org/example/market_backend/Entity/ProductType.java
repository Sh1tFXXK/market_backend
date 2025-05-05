package org.example.market_backend.Entity;

import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 商品类别表
 * @TableName product_type
 */
@Setter
@Getter
@Table(name="product_type")
public class ProductType {
    /**
     * 商品类别编号
     * -- GETTER --
     *  商品类别编号
     * -- SETTER --
     *  商品类别编号


     */
    @Id
    private Integer id;

    /**
     * 商品图片，多个图片地址用英文逗号隔开
     * -- GETTER --
     *  商品图片，多个图片地址用英文逗号隔开
     * -- SETTER --
     *  商品图片，多个图片地址用英文逗号隔开


     */
    private String productImgs;

    /**
     * 商品描述
     * -- GETTER --
     *  商品描述
     * -- SETTER --
     *  商品描述


     */
    private String productDesc;

    /**
     * 商品价格
     * -- GETTER --
     *  商品价格
     * -- SETTER --
     *  商品价格


     */
    private BigDecimal productPrice;

    /**
     * 商品类别
     * -- GETTER --
     *  商品类别
     * -- SETTER --
     *  商品类别


     */
    private Integer productTypeId;

    /**
     * 发布人
     * -- GETTER --
     *  发布人
     * -- SETTER --
     *  发布人


     */
    private String publishUserId;

    /**
     * 商品发布时的地理位置
     * -- GETTER --
     *  商品发布时的地理位置
     * -- SETTER --
     *  商品发布时的地理位置


     */
    private String productAddress;

    /**
     * 创建时间
     * -- GETTER --
     *  创建时间
     * -- SETTER --
     *  创建时间


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
     * 备注
     * -- GETTER --
     *  备注
     * -- SETTER --
     *  备注


     */
    private String remark;

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        ProductType other = (ProductType) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getProductImgs() == null ? other.getProductImgs() == null : this.getProductImgs().equals(other.getProductImgs()))
            && (this.getProductDesc() == null ? other.getProductDesc() == null : this.getProductDesc().equals(other.getProductDesc()))
            && (this.getProductPrice() == null ? other.getProductPrice() == null : this.getProductPrice().equals(other.getProductPrice()))
            && (this.getProductTypeId() == null ? other.getProductTypeId() == null : this.getProductTypeId().equals(other.getProductTypeId()))
            && (this.getPublishUserId() == null ? other.getPublishUserId() == null : this.getPublishUserId().equals(other.getPublishUserId()))
            && (this.getProductAddress() == null ? other.getProductAddress() == null : this.getProductAddress().equals(other.getProductAddress()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()))
            && (this.getRemark() == null ? other.getRemark() == null : this.getRemark().equals(other.getRemark()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getProductImgs() == null) ? 0 : getProductImgs().hashCode());
        result = prime * result + ((getProductDesc() == null) ? 0 : getProductDesc().hashCode());
        result = prime * result + ((getProductPrice() == null) ? 0 : getProductPrice().hashCode());
        result = prime * result + ((getProductTypeId() == null) ? 0 : getProductTypeId().hashCode());
        result = prime * result + ((getPublishUserId() == null) ? 0 : getPublishUserId().hashCode());
        result = prime * result + ((getProductAddress() == null) ? 0 : getProductAddress().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        result = prime * result + ((getRemark() == null) ? 0 : getRemark().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", productImgs=").append(productImgs);
        sb.append(", productDesc=").append(productDesc);
        sb.append(", productPrice=").append(productPrice);
        sb.append(", productTypeId=").append(productTypeId);
        sb.append(", publishUserId=").append(publishUserId);
        sb.append(", productAddress=").append(productAddress);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", remark=").append(remark);
        sb.append("]");
        return sb.toString();
    }
}