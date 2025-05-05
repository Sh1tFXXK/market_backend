package org.example.market_backend.Entity;



import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.Date;

/**
 * 评论回复表
 * @TableName comment_reply
 */
@Setter
@Getter
@Table(name = "praise")
public class Praise {
    /**
     * 评论编号主键
     * -- GETTER --
     *  评论编号主键
     * -- SETTER --
     *  评论编号主键


     */
    @Id
    @Column(name = "id")
    private Integer id;

    /**
     * 商品编号
     * -- GETTER --
     *  商品编号
     * -- SETTER --
     *  商品编号


     */
    @Column(name = "product_id")
    private Integer productId;

    /**
     * 点赞用户编号
     * -- GETTER --
     *  点赞用户编号
     * -- SETTER --
     *  点赞用户编号


     */
    @Column(name = "user_id")
    private String userId;

    /**
     * 昵称
     * -- GETTER --
     *  昵称
     * -- SETTER --
     *  昵称


     */
    @Column(name = "user_name")
    private String userName;

    /**
     * 头像
     * -- GETTER --
     *  头像
     * -- SETTER --
     *  头像


     */
    @Column(name = "user_avatar")
    private String userAvatar;

    /**
     * 点赞状态1 -点赞 2 ---取消点赞
     * -- GETTER --
     *  点赞状态1 -点赞 2 ---取消点赞
     * -- SETTER --
     *  点赞状态1 -点赞 2 ---取消点赞


     */
    @Column(name = "status")
    private Integer status;

    /**
     * 点赞状态/取消点赞时间
     * -- GETTER --
     *  点赞状态/取消点赞时间
     * -- SETTER --
     *  点赞状态/取消点赞时间


     */
    @Column(name = "praise_time")
    private Date praiseTime;



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
        Praise other = (Praise) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getProductId() == null ? other.getProductId() == null : this.getProductId().equals(other.getProductId()))
            && (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()))
            && (this.getUserName() == null ? other.getUserName() == null : this.getUserName().equals(other.getUserName()))
            && (this.getUserAvatar() == null ? other.getUserAvatar() == null : this.getUserAvatar().equals(other.getUserAvatar()))
            && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
            && (this.getPraiseTime() == null ? other.getPraiseTime() == null : this.getPraiseTime().equals(other.getPraiseTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getProductId() == null) ? 0 : getProductId().hashCode());
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        result = prime * result + ((getUserName() == null) ? 0 : getUserName().hashCode());
        result = prime * result + ((getUserAvatar() == null) ? 0 : getUserAvatar().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getPraiseTime() == null) ? 0 : getPraiseTime().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", productId=").append(productId);
        sb.append(", userId=").append(userId);
        sb.append(", userName=").append(userName);
        sb.append(", userAvatar=").append(userAvatar);
        sb.append(", status=").append(status);
        sb.append(", praiseTime=").append(praiseTime);
        sb.append("]");
        return sb.toString();
    }
}