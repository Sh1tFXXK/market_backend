package org.example.market_backend.Mapper;

import org.example.market_backend.BO.OrderBO;
import org.example.market_backend.Entity.Order;



/**
* @author 31414
* @description 针对表【order(订单表)】的数据库操作Mapper
* @createDate 2025-05-03 17:27:00
* @Entity .Order
*/
public interface OrderMapper {

    int deleteByPrimaryKey(Long id);

    int insert(Order record);

    int insertSelective(Order record);

    Order selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(Order record);

    int updateByPrimaryKey(Order record);

    OrderBO selectOrderDetailByOrderId(String orderId);
}
