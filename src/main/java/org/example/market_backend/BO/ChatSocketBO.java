package org.example.market_backend.BO;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString(callSuper = true)
public class ChatSocketBO {
    private String toUserId;
    private String toUserAvatar;
    private String toUserName;
}
