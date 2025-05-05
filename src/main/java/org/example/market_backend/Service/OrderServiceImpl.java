package org.example.market_backend.Service;

import org.example.market_backend.BO.OrderBO;
import org.example.market_backend.BO.ProductBO;
import org.example.market_backend.Constants.CodeEnum;
import org.example.market_backend.Constants.RedisConstants;
import org.example.market_backend.Entity.Address;
import org.example.market_backend.Entity.Order;
import org.example.market_backend.Entity.User;
import org.example.market_backend.Exception.BusinessException;

import org.example.market_backend.Mapper.AddressMapper;
import org.example.market_backend.Mapper.OrderMapper;
import org.example.market_backend.Mapper.ProductMapper;
import org.example.market_backend.Utils.DirectMqSender;
import org.example.market_backend.Utils.RedisUtils;
import org.example.market_backend.VO.CommonOrderVO;
import org.example.market_backend.VO.OrderVO;
import org.slf4j.Logger;
import org.springframework.util.Assert;
import org.example.market_backend.Constants.RabbitMqEnum;

import java.util.Date;

public class OrderServiceImpl implements OrderService {
    private UserService userService;
    private OrderMapper orderMapper;
    private Logger log;
    private ProductMapper productMapper;
    private AddressMapper addressMapper;
    private DirectMqSender directMqSender;
    /**
     * 购买商品
     */
    @Override
    public OrderBO placeOrder(OrderVO vo) {
        try {
            //检验token
            User user = userService.checkToken(vo.getToken());
            //检验商品是否存在
            ProductBO productBO = productMapper.selectProductInfoAndTradeStatus(vo.getProductId());
            Assert.notNull(productBO, "商品不存在");
            Assert.isTrue(productBO.getTradeStatus() == 2, "商品已被购买或以卖出");
            Address address = addressMapper.selectByPrimaryKey(vo.getAddressId());
            Assert.notNull(address, "收货地址不存在");
            Assert.isTrue(address.getStatus() == 1, "收货地址已被删除");
            //设置redis锁，防止商品被重复交易
            long productLock = RedisUtils.setNX(String.format(RedisConstants.PRODUCT_ID, vo.getProductId()), 60, "1");
            if (productLock == 0) {
                log.info("商品正在被交易，{}", vo.getProductId());
                throw new BusinessException(CodeEnum.PRODUCT_IN_TRADE);
            }
            Date date = new Date();
            //生成待支付订单
            Order order = new Order();
            order.setBuyingUserId(user.getUserId());
            order.setCreateTime(date);
            order.setOrderAmount(productBO.getProductPrice());
            order.setProductId(productBO.getId());
            order.setUpdateTime(date);
            String consigneeAddreess = address.getProvince() + " " + address.getCity() + address.getDistrict() + address.getStreet() + " " + address.getDetail();
            order.setConsigneeAddress(consigneeAddreess);
            order.setConsigneeMobile(address.getConsigneeMobile());
            order.setConsigneeName(address.getConsigneeName());
            orderMapper.insertSelective(order);
            String orderId = order.getOrderId();
            //发送延时队列，用于订单超过24小时未处理自动取消
            directMqSender.sendTtlMessage(RabbitMqEnum.RoutingEnum.SECONDARY_ORDER_DEAD_LETTER_ROUTING, orderId + "");
            //订单有效时长写入redis
            String key = String.format(RedisConstants.ORDER_ID, orderId);
            Long ttl = RedisUtils.getExpire(key);
            RedisUtils.get(key, ttl / 1000, order + "");
            //组装返回数据
            OrderBO bo = new OrderBO();
            bo.setOrderId(orderId);
            bo.setCreateTime(date);
            bo.setOrderAmount(order.getOrderAmount());
            bo.setPayStatus(1);
            bo.setTradeStatus(1);
            bo.setProductDesc(productBO.getProductDesc());
            bo.setProductImgs(productBO.getProductImgs());
            bo.setBuyerUserName(user.getUserName());
            bo.setExpiredTime((long) ttl);
            return bo;
        } catch (BusinessException e) {
            RedisUtils.delete(String.format(RedisConstants.PRODUCT_ID, vo.getProductId()));
            throw e;
        } catch (Exception e) {
            RedisUtils.delete(String.format(RedisConstants.PRODUCT_ID, vo.getProductId()));
            log.error("购买商品失败", e);
            throw new BusinessException(CodeEnum.FAIL);
        }
    }

    @Override
    public OrderBO getOrderDetails(CommonOrderVO vo) {
        userService.checkToken(vo.getToken());
        OrderBO bo = orderMapper.selectOrderDetailByOrderId(vo.getOrderId());
        Assert.notNull(bo, "订单不存在");
        String key = String.format(RedisConstants.ORDER_ID, vo.getOrderId());
        long expiredTime = RedisUtils.pttl(key);
        log.info("订单有效时长：{}", expiredTime);
        bo.setExpiredTime(expiredTime < 0 ? 0 : expiredTime / 1000);
        return bo;
    }

    @Override
    public void cancelOrder(CommonOrderVO vo) {
        userService.checkToken(vo.getToken());
        Order order = orderMapper.selectByPrimaryKey(vo.getOrderId());
        Assert.notNull(order, "订单不存在");
        Assert.isTrue(order.getTradeStatus() == 1, "订单已取消或结算");
        Assert.isTrue(order.getPayStatus() == 1 || order.getPayStatus() == 4, "订单已支付");
        Order update = new Order();
        update.setOrderId(vo.getOrderId());
        update.setTradeStatus(2);
        update.setUpdateTime(new Date());
        orderMapper.updateByPrimaryKeySelective(update);
    }
}
