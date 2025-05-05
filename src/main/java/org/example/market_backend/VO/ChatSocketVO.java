package org.example.market_backend.VO;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString(callSuper = true)
public class ChatSocketVO extends BaseVO{
    private static final long serialVersionUID = 1L;

    private String toUserId;
    private String toUserName;
    private String toUserAvatar;
    private  String content;
    private String chatId;
}
