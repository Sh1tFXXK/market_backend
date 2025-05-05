package org.example.market_backend.Entity;

import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.Date;

/**
 * 评论回复表
 * @TableName comment_reply
 */
@Setter
@Getter
@Table(name ="comment_reply")
public class CommentReply {
    /**
     * 评论编号主键
     * -- GETTER --
     *  评论编号主键
     * -- SETTER --
     *  评论编号主键


     */
    @Id
    private Integer id;

    /**
     * 商品编号
     * -- GETTER --
     *  商品编号
     * -- SETTER --
     *  商品编号


     */
    private Integer productId;

    /**
     * 评论内容
     * -- GETTER --
     *  评论内容
     * -- SETTER --
     *  评论内容


     */
    private String content;

    /**
     * 评论/回复用户编号
     * -- GETTER --
     *  评论/回复用户编号
     * -- SETTER --
     *  评论/回复用户编号


     */
    private String fromUserId;

    /**
     * 评论/回复用户昵称
     * -- GETTER --
     *  评论/回复用户昵称
     * -- SETTER --
     *  评论/回复用户昵称


     */
    private String fromUserName;

    /**
     * 评论/回复头像
     * -- GETTER --
     *  评论/回复头像
     * -- SETTER --
     *  评论/回复头像


     */
    private String fromUserAvatar;

    /**
     * 评论/回复时间
     * -- GETTER --
     *  评论/回复时间
     * -- SETTER --
     *  评论/回复时间


     */
    private Date createTime;

    /**
     * 类型 1-评论 2-回复
     * -- GETTER --
     *  类型 1-评论 2-回复
     * -- SETTER --
     *  类型 1-评论 2-回复


     */
    private Integer type;

    /**
     * 回复人编号
     * -- GETTER --
     *  回复人编号
     * -- SETTER --
     *  回复人编号


     */
    private String toUserId;

    /**
     * 回复人昵称
     * -- GETTER --
     *  回复人昵称
     * -- SETTER --
     *  回复人昵称


     */
    private String toUserName;

    /**
     * 回复人头像
     * -- GETTER --
     *  回复人头像
     * -- SETTER --
     *  回复人头像


     */
    private String toUserAvatar;

    /**
     * 回复编号
     * -- GETTER --
     *  回复编号
     * -- SETTER --
     *  回复编号


     */
    private Integer replyId;

}