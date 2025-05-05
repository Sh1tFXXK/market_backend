package org.example.market_backend.BO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.example.market_backend.Entity.ChatList;

import java.io.Serializable;
import java.math.BigDecimal;

@Setter
@Getter
@ToString(callSuper = true)
public class ChatBO extends ChatList implements Serializable {
    private static final long serialVersionUID = 1L;
    private String lastChatContent;
    private Integer productId;
    private String productImgs;
    private BigDecimal productPrice;
}
