package org.example.market_backend.Entity;


import com.alibaba.fastjson.JSON;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.example.market_backend.Service.ChatService;
import org.example.market_backend.Utils.SpringContextUtil;
import org.example.market_backend.VO.ChatSocketVO;
import org.springframework.stereotype.Component;

import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.OnClose;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@ServerEndpoint("/socket")
@Component
@Slf4j
@EqualsAndHashCode
public class ChatSocket {
    /**
     * 静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
     */
    private static AtomicInteger onlineCount = new AtomicInteger(0);

    /**
     * ConcurrentHashMap，用来存放每个客户端对应的MyWebSocket对象。
     */
    private static ConcurrentHashMap<String, ChatSocket> concurrentHashMap = new ConcurrentHashMap<>(16);
    /**
     * 与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    private Session session;
    /**
     * 连接建立成功后调用的方法
     */
    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        //加入map
        concurrentHashMap.put(session.getId(), this);
        //在线数加1
        onlineCount.incrementAndGet();
        log.info("有新连接加入！当前在线人数为：{}", onlineCount.get());
        try {
            this.session.getBasicRemote().sendText("连接成功");
        } catch (Exception e) {
            log.error("websocket IO异常");
        }
    }

    @OnClose
    public void onClose() {
        concurrentHashMap.remove(session.getId());
        //在线数减1
        onlineCount.decrementAndGet();
        log.info("有一连接关闭！当前在线人数为：{}", onlineCount.get());
    }

    @OnMessage
    public void onMessage (String message) throws IOException{
        log.info("来自客户端的消息：{}", message);
        //防止将心跳数据入库并发送
        String reg ="{";
        //消息是否入库成功
        boolean send = false;
        if(message.contains(reg)){
            ChatSocketVO vo = JSON.parseObject(message, ChatSocketVO.class);
            try{
                ChatService chatService = SpringContextUtil.getBean(ChatService.class);
                send =chatService.chat(vo);
            }catch (Exception e){
                log.warn("入库失败");
            }
            if(send){
                sendInfo(vo);
            }
        }
    }
    @OnError
    public void onError(Throwable error){
        log.error("发生错误：{}", error);
        error.printStackTrace();
    }

    private void sendMessage(String message) throws IOException{
        this.session.getBasicRemote().sendText(message);
    }

    private static void sendInfo(ChatSocketVO vo) throws IOException{
        //获取唯一通道
        ChatSocket  socket = concurrentHashMap.get(vo.getChatId());
        String message = JSON.toJSONString(vo);
        if(socket!=null){
            log.info("消息发送成功");
            socket.sendMessage(message);
        }else {
            log.info("消息发送失败");
        }
    }
    public boolean online(String userId){
        return concurrentHashMap.containsKey(userId);
    }
    private static synchronized int getOnlineCount() {
        return onlineCount.get();
    }
    private static synchronized void addOnlineCount() {
        onlineCount.incrementAndGet();
    }
    private static synchronized void subOnlineCount() {
        onlineCount.decrementAndGet();
    }
}
