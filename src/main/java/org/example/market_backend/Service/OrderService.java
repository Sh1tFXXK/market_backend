package org.example.market_backend.Service;

import org.example.market_backend.BO.OrderBO;
import org.example.market_backend.VO.CommonOrderVO;
import org.example.market_backend.VO.OrderVO;

public interface OrderService {

    OrderBO placeOrder(OrderVO vo);

    OrderBO getOrderDetails(CommonOrderVO vo);

    void cancelOrder(CommonOrderVO vo);
}
