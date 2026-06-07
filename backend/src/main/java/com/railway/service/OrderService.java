package com.railway.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.railway.dto.BuyTicketDTO;
import com.railway.entity.Order;
import com.railway.vo.OrderVO;

import java.util.List;

/**
 * 订单服务接口
 */
public interface OrderService extends IService<Order> {

    /**
     * 购票下单
     */
    OrderVO buyTicket(Long userId, BuyTicketDTO buyTicketDTO);

    /**
     * 支付订单
     */
    void payOrder(Long userId, Long orderId, Integer payType);

    /**
     * 取消订单
     */
    void cancelOrder(Long userId, Long orderId);

    /**
     * 获取订单列表
     */
    List<OrderVO> getOrderList(Long userId, Integer status);

    /**
     * 获取订单详情
     */
    OrderVO getOrderDetail(Long userId, Long orderId);

    /**
     * 退票
     */
    void refundTicket(Long userId, Long orderId, Long itemId);

    /**
     * 改签
     */
    OrderVO changeTicket(Long userId, Long orderId, BuyTicketDTO changeDTO);
}