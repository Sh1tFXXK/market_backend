package org.example.market_backend.controller;

import org.example.market_backend.BO.ResponseBO;
import org.example.market_backend.Entity.User;
import org.example.market_backend.Service.ChatService;
import org.example.market_backend.Utils.RedisUtils;
import org.example.market_backend.Utils.TokenUtils;
import org.example.market_backend.VO.BaseVO;
import org.example.market_backend.VO.ChatDetailVO;
import org.example.market_backend.VO.InitChatVO;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RequestMapping("/chat")
@RestController
public class ChatController {
    private ChatService chatService;
    private User user;
    /**
     * 查询聊天会话框列表数据
     */
    @RequestMapping(value = "getChatList", method = RequestMethod.POST )
    public ResponseBO getChatList(BaseVO vo) {
        String token = checkToken();
        vo.setToken(token);
        return ResponseBO.successPageInfo(chatService.getChatList(vo));
    }
    /**
     * 查询聊天记录
     */
    @RequestMapping(value = "getChatDetailList",method = RequestMethod.POST)
    public ResponseBO getChatDetailList(@RequestBody BaseVO vo) {
        String token = checkToken();
        vo.setToken(token);
        Assert.notEmpty(Collections.singleton(vo.getChatId()),"聊天会话框ID不能为空");
        return ResponseBO.successPageInfo(chatService.getChatDetailList(vo));
    }
    /**
     * 初始化聊天
     */
    @RequestMapping(value = "initChat",method = RequestMethod.POST)
    public ResponseBO initChat(@RequestBody InitChatVO vo){
        //检查token
        String token = checkToken();
        vo.setToken(token);
        //检验发送对象用户编号是否为空
        Assert.notEmpty(Collections.singleton(vo.getToUserId()),"发送对象用户编号不能为空");
        Assert.notEmpty(Collections.singleton(vo.getProductId()),"商品编号不能为空");
        String chatId = chatService.init(vo);
        Map<String, Object> map= new HashMap<>(16);
        map.put("chatId",chatId);
        return ResponseBO.success(map);
    }
    private String checkToken(){
        String token = RedisUtils.get(TokenUtils.getToken(user.getUserId()));
        Assert.notNull(token, "用户未登录");
        return token;
    }
}
