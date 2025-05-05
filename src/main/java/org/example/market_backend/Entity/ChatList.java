package org.example.market_backend.Entity;


import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.Date;

/**
 * 聊天列表
 * @TableName chat_list
 */
@Setter
@Getter
@Table(name ="chat_list")
public class ChatList {
    /**
     * 主表自增主键
     * -- SETTER --
     *  主表自增主键
     * -- GETTER --
     *  主表自增主键


     */
    @Getter
    @Setter
    @Id
    private Integer id;

    /**
     * 聊天主表主键
     */
    private String chatId;

    /**
     * 商品编号
     */
    private Integer pruductId;

    /**
     * 当前用户编号
     */
    private String userId;

    /**
     * 当前用户昵称
     */
    private String userName;

    /**
     * 当前用户头像
     */
    private String userAvatar;

    /**
     * 是否在线 1-是 2 否
     */
    private Integer isOnline;

    /**
     * 未读数
     */
    private Integer unread;

    /**
     * 状态  1有效 2删除
     */
    private Integer status;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 对方用户编号
     */
    private String anotherUserId;

    /**
     * 对方用户昵称
     */
    private String anotherUserName;

    /**
     * 对方用户头像
     */
    private String anotherUserAvatar;


}