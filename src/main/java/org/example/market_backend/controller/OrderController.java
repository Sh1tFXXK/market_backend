package org.example.market_backend.controller;

import org.example.market_backend.BO.ResponseBO;
import org.example.market_backend.Entity.User;
import org.example.market_backend.Service.OrderService;
import org.example.market_backend.Utils.RedisUtils;
import org.example.market_backend.Utils.TokenUtils;
import org.example.market_backend.VO.CommonOrderVO;
import org.example.market_backend.VO.OrderVO;
import org.springframework.http.MediaType;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


import java.awt.*;
import java.util.Collections;

public class OrderController {
    private OrderService orderService;
    private User user;
    /**
     * 购买商品
     */
    @RequestMapping(value = "placeOrder" , method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseBO placeOrder(@RequestBody OrderVO vo){
        String token = checkToken();
        Assert.notNull(vo.getProductId(),"商品ID不能为空");
        Assert.notNull(vo.getAddressId(),"收货地址不能为空");
        vo.setToken(token);
        orderService.placeOrder(vo);
        return ResponseBO.success();
    }
    /**
     * 获取订单列表
     */
    @RequestMapping(value = "getOrderDetails",method = RequestMethod.GET)
    public ResponseBO getOrderDetails(CommonOrderVO vo){
        String token = checkToken();
        Assert.notNull(vo.getOrderId(),"订单ID不能为空");
        vo.setToken(token);
        orderService.getOrderDetails(vo);
        return ResponseBO.success();
    }
    /**
     * 取消订单
     */
    @RequestMapping(value = "cancelOrder",method = RequestMethod.POST)
    public ResponseBO cancelOrder(@RequestBody CommonOrderVO vo){
        String token = checkToken();
        Assert.notEmpty(Collections.singleton(vo.getOrderId()),"订单ID不能为空");
        vo.setToken(token);
        orderService.cancelOrder(vo);
        return ResponseBO.success();
    }
    private String checkToken(){
        String token = RedisUtils.get(TokenUtils.getToken(user.getUserId()));
        Assert.notNull(token, "用户未登录");
        return token;
    }
}
