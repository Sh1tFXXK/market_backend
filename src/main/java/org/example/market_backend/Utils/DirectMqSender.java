package org.example.market_backend.Utils;

import org.example.market_backend.Constants.RabbitMqEnum;

public class DirectMqSender {
    public void sendTtlMessage(RabbitMqEnum rabbitMqEnum, String s) {
        System.out.println("发送消息："+s);
    }


}
