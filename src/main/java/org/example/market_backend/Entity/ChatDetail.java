package org.example.market_backend.Entity;


import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.Date;

/**
 * 聊天详情表
 * @TableName chat_detail
 */
@Table(name ="chat_detail")
public class ChatDetail {
    /**
     * 自增主键
     */
    @Id
    private Integer id;

    /**
     * 聊天主表编号
     */
    private String chatId;

    /**
     * 消息发送者编号
     */
    private String userId;

    /**
     * 消息发送者昵称
     */
    private String userName;

    /**
     * 消息发送者头像
     */
    private String userAvatar;

    /**
     * 发送内容
     */
    private String content;

    /**
     * 消息类型 1 用户消息 2 系统消息
     */
    private Integer type;

    /**
     * 是否最后一条消息 1是 2  否
     */
    private Integer isLastest;

    /**
     * 创建时间
     */
    private Date caeateTime;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 自增主键
     */
    public Integer getId() {
        return id;
    }

    /**
     * 自增主键
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 聊天主表编号
     */
    public String getChatId() {
        return chatId;
    }

    /**
     * 聊天主表编号
     */
    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    /**
     * 消息发送者编号
     */
    public String getUserId() {
        return userId;
    }

    /**
     * 消息发送者编号
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * 消息发送者昵称
     */
    public String getUserName() {
        return userName;
    }

    /**
     * 消息发送者昵称
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * 消息发送者头像
     */
    public String getUserAvatar() {
        return userAvatar;
    }

    /**
     * 消息发送者头像
     */
    public void setUserAvatar(String userAvatar) {
        this.userAvatar = userAvatar;
    }

    /**
     * 发送内容
     */
    public String getContent() {
        return content;
    }

    /**
     * 发送内容
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * 消息类型 1 用户消息 2 系统消息
     */
    public Integer getType() {
        return type;
    }

    /**
     * 消息类型 1 用户消息 2 系统消息
     */
    public void setType(Integer type) {
        this.type = type;
    }

    /**
     * 是否最后一条消息 1是 2  否
     */
    public Integer getIsLastest() {
        return isLastest;
    }

    /**
     * 是否最后一条消息 1是 2  否
     */
    public void setIsLastest(Integer isLastest) {
        this.isLastest = isLastest;
    }

    /**
     * 创建时间
     */
    public Date getCaeateTime() {
        return caeateTime;
    }

    /**
     * 创建时间
     */
    public void setCaeateTime(Date caeateTime) {
        this.caeateTime = caeateTime;
    }

    /**
     * 修改时间
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * 修改时间
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}