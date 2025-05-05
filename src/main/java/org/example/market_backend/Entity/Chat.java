package org.example.market_backend.Entity;


import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * 聊天主表
 * @TableName chat
 */
@Table(name ="chat")
public class Chat {
    /**
     * 聊天主表编号
     */
    @Id
    private String id;

    /**
     * 用户编号
     */
    private String userId;

    /**
     * 对方用户编号
     */
    private String anotherUserId;

    /**
     * 商品编号
     */
    private Integer productId;

    /**
     * 聊天主表编号
     */
    public String getId() {
        return id;
    }

    /**
     * 聊天主表编号
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * 用户编号
     */
    public String getUserId() {
        return userId;
    }

    /**
     * 用户编号
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * 对方用户编号
     */
    public String getAnotherUserId() {
        return anotherUserId;
    }

    /**
     * 对方用户编号
     */
    public void setAnotherUserId(String anotherUserId) {
        this.anotherUserId = anotherUserId;
    }

    /**
     * 商品编号
     */
    public Integer getProductId() {
        return productId;
    }

    /**
     * 商品编号
     */
    public void setProductId(Integer productId) {
        this.productId = productId;
    }
}