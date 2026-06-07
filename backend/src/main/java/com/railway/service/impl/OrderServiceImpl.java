package com.railway.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.railway.dto.BuyTicketDTO;
import com.railway.entity.*;
import com.railway.mapper.OrderItemMapper;
import com.railway.mapper.OrderMapper;
import com.railway.mapper.PassengerMapper;
import com.railway.mapper.TrainMapper;
import com.railway.service.OrderService;
import com.railway.vo.OrderVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * 订单服务实现
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    private final OrderItemMapper orderItemMapper;
    private final PassengerMapper passengerMapper;
    private final TrainMapper trainMapper;

    public OrderServiceImpl(OrderItemMapper orderItemMapper, PassengerMapper passengerMapper, TrainMapper trainMapper) {
        this.orderItemMapper = orderItemMapper;
        this.passengerMapper = passengerMapper;
        this.trainMapper = trainMapper;
    }

    @Override
    @Transactional
    public OrderVO buyTicket(Long userId, BuyTicketDTO buyTicketDTO) {
        // 查询车次信息
        Train train = trainMapper.selectById(buyTicketDTO.getTrainId());
        if (train == null) {
            throw new RuntimeException("车次不存在");
        }

        // 查询乘客信息
        List<Long> passengerIds = buyTicketDTO.getPassengers().stream()
                .map(BuyTicketDTO.PassengerInfo::getPassengerId)
                .collect(Collectors.toList());

        List<Passenger> passengers = passengerMapper.selectBatchIds(passengerIds);
        if (passengers.size() != passengerIds.size()) {
            throw new RuntimeException("部分乘客不存在");
        }

        // 检查乘客是否属于当前用户
        for (Passenger passenger : passengers) {
            if (!passenger.getUserId().equals(userId)) {
                throw new RuntimeException("无权为该乘客购票");
            }
        }

        // 创建订单
        Order order = new Order();
        order.setOrderNo(generateOrderNo());
        order.setUserId(userId);
        order.setTrainId(train.getId());
        order.setTrainCode(train.getTrainCode());
        order.setTravelDate(LocalDate.parse(buyTicketDTO.getTravelDate()));
        order.setStartStationName(buyTicketDTO.getStartStation());
        order.setEndStationName(buyTicketDTO.getEndStation());
        order.setStartTime(train.getStartTime());
        order.setEndTime(train.getEndTime());
        order.setPassengerCount(passengers.size());
        order.setOrderStatus(0); // 待支付

        // 计算总价（模拟票价）
        BigDecimal totalPrice = BigDecimal.valueOf(passengers.size() * 100.0);
        order.setTotalPrice(totalPrice);

        this.save(order);

        // 创建订单明细
        List<OrderItem> items = new ArrayList<>();
        BigDecimal seatPrice = BigDecimal.valueOf(100.0); // 模拟票价

        for (int i = 0; i < passengers.size(); i++) {
            Passenger passenger = passengers.get(i);
            BuyTicketDTO.PassengerInfo passengerInfo = buyTicketDTO.getPassengers().get(i);

            OrderItem item = new OrderItem();
            item.setOrderId(order.getId());
            item.setOrderNo(order.getOrderNo());
            item.setPassengerId(passenger.getId());
            item.setPassengerName(passenger.getName());
            item.setPassengerIdCard(passenger.getIdCard());
            item.setPassengerType(passenger.getPassengerType());
            item.setSeatTypeName("二等座"); // 模拟
            item.setPrice(seatPrice);
            item.setTicketStatus(0);
            item.setTicketNo(generateTicketNo());

            if (passengerInfo.getSeatNumber() != null) {
                item.setSeatNumber(passengerInfo.getSeatNumber());
                item.setCarriageNumber(Integer.parseInt(passengerInfo.getSeatNumber().substring(0, 2)));
            }

            items.add(item);
        }

        for (OrderItem item : items) {
            orderItemMapper.insert(item);
        }

        return convertToVO(order, items);
    }

    @Override
    @Transactional
    public void payOrder(Long userId, Long orderId, Integer payType) {
        Order order = this.lambdaQuery()
                .eq(Order::getId, orderId)
                .eq(Order::getUserId, userId)
                .one();

        if (order == null) {
            throw new RuntimeException("订单不存在");
        }

        if (order.getOrderStatus() != 0) {
            throw new RuntimeException("订单状态不正确");
        }

        order.setPayPrice(order.getTotalPrice());
        order.setPayTime(LocalDateTime.now());
        order.setPayType(payType);
        order.setOrderStatus(1); // 已支付

        this.updateById(order);

        // 更新订单明细状态
        List<OrderItem> items = orderItemMapper.selectList(
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<OrderItem>()
                        .eq(OrderItem::getOrderId, orderId)
        );

        for (OrderItem item : items) {
            item.setTicketStatus(1); // 已支付
            orderItemMapper.updateById(item);
        }
    }

    @Override
    @Transactional
    public void cancelOrder(Long userId, Long orderId) {
        Order order = this.lambdaQuery()
                .eq(Order::getId, orderId)
                .eq(Order::getUserId, userId)
                .one();

        if (order == null) {
            throw new RuntimeException("订单不存在");
        }

        if (order.getOrderStatus() != 0) {
            throw new RuntimeException("只能取消未支付的订单");
        }

        order.setOrderStatus(2); // 已取消
        order.setCancelTime(LocalDateTime.now());
        order.setCancelReason("用户主动取消");

        this.updateById(order);
    }

    @Override
    public List<OrderVO> getOrderList(Long userId, Integer status) {
        List<Order> orders;

        if (status != null) {
            orders = this.lambdaQuery()
                    .eq(Order::getUserId, userId)
                    .eq(Order::getOrderStatus, status)
                    .orderByDesc(Order::getCreateTime)
                    .list();
        } else {
            orders = this.lambdaQuery()
                    .eq(Order::getUserId, userId)
                    .orderByDesc(Order::getCreateTime)
                    .list();
        }

        return orders.stream()
                .map(order -> {
                    List<OrderItem> items = orderItemMapper.selectList(
                            new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<OrderItem>()
                                    .eq(OrderItem::getOrderId, order.getId())
                    );
                    return convertToVO(order, items);
                })
                .collect(Collectors.toList());
    }

    @Override
    public OrderVO getOrderDetail(Long userId, Long orderId) {
        Order order = this.lambdaQuery()
                .eq(Order::getId, orderId)
                .eq(Order::getUserId, userId)
                .one();

        if (order == null) {
            throw new RuntimeException("订单不存在");
        }

        List<OrderItem> items = orderItemMapper.selectList(
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<OrderItem>()
                        .eq(OrderItem::getOrderId, orderId)
        );

        return convertToVO(order, items);
    }

    @Override
    @Transactional
    public void refundTicket(Long userId, Long orderId, Long itemId) {
        Order order = this.lambdaQuery()
                .eq(Order::getId, orderId)
                .eq(Order::getUserId, userId)
                .one();

        if (order == null) {
            throw new RuntimeException("订单不存在");
        }

        if (order.getOrderStatus() != 1) {
            throw new RuntimeException("只能退已支付的订单");
        }

        OrderItem item = orderItemMapper.selectById(itemId);
        if (item == null || !item.getOrderId().equals(orderId)) {
            throw new RuntimeException("车票不存在");
        }

        // 计算退票手续费（模拟）
        BigDecimal refundFee = item.getPrice().multiply(BigDecimal.valueOf(0.05)); // 5%手续费
        BigDecimal refundAmount = item.getPrice().subtract(refundFee);

        item.setTicketStatus(2); // 已退票
        orderItemMapper.updateById(item);

        // 更新订单状态（如果全部退票则订单状态改为已退票）
        List<OrderItem> allItems = orderItemMapper.selectList(
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<OrderItem>()
                        .eq(OrderItem::getOrderId, orderId)
        );

        boolean allRefunded = allItems.stream()
                .allMatch(i -> i.getTicketStatus() == 2);

        if (allRefunded) {
            order.setOrderStatus(4); // 已退票
            this.updateById(order);
        }
    }

    @Override
    @Transactional
    public OrderVO changeTicket(Long userId, Long orderId, BuyTicketDTO changeDTO) {
        Order order = this.lambdaQuery()
                .eq(Order::getId, orderId)
                .eq(Order::getUserId, userId)
                .one();

        if (order == null) {
            throw new RuntimeException("订单不存在");
        }

        if (order.getOrderStatus() != 1) {
            throw new RuntimeException("只能改签已支付的订单");
        }

        // 创建新订单（简化处理）
        OrderVO newOrder = buyTicket(userId, changeDTO);

        // 更新原订单状态
        order.setOrderStatus(5); // 已改签
        this.updateById(order);

        return newOrder;
    }

    /**
     * 转换为VO
     */
    private OrderVO convertToVO(Order order, List<OrderItem> items) {
        OrderVO vo = new OrderVO();
        vo.setOrderId(order.getId());
        vo.setOrderNo(order.getOrderNo());
        vo.setTrainCode(order.getTrainCode());
        vo.setTravelDate(order.getTravelDate());
        vo.setStartStationName(order.getStartStationName());
        vo.setEndStationName(order.getEndStationName());
        vo.setStartTime(order.getStartTime());
        vo.setEndTime(order.getEndTime());
        vo.setPassengerCount(order.getPassengerCount());
        vo.setTotalPrice(order.getTotalPrice());
        vo.setPayPrice(order.getPayPrice());
        vo.setOrderStatus(order.getOrderStatus());
        vo.setOrderStatusName(getOrderStatusName(order.getOrderStatus()));
        vo.setCreateTime(order.getCreateTime());
        vo.setPayTime(order.getPayTime());

        List<OrderVO.OrderItemVO> itemVOs = items.stream()
                .map(item -> {
                    OrderVO.OrderItemVO itemVO = new OrderVO.OrderItemVO();
                    itemVO.setItemId(item.getId());
                    itemVO.setPassengerName(item.getPassengerName());
                    itemVO.setPassengerIdCard(item.getPassengerIdCard());
                    itemVO.setSeatTypeName(item.getSeatTypeName());
                    itemVO.setSeatNumber(item.getSeatNumber());
                    itemVO.setCarriageNumber(item.getCarriageNumber());
                    itemVO.setPrice(item.getPrice());
                    itemVO.setTicketNo(item.getTicketNo());
                    itemVO.setTicketStatus(item.getTicketStatus());
                    return itemVO;
                })
                .collect(Collectors.toList());

        vo.setItems(itemVOs);
        return vo;
    }

    /**
     * 生成订单号
     */
    private String generateOrderNo() {
        return "E" + System.currentTimeMillis() + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    /**
     * 生成票号
     */
    private String generateTicketNo() {
        return "T" + System.currentTimeMillis() + UUID.randomUUID().toString().substring(0, 6).toUpperCase();
    }

    /**
     * 获取订单状态名称
     */
    private String getOrderStatusName(Integer status) {
        switch (status) {
            case 0: return "待支付";
            case 1: return "已支付";
            case 2: return "已取消";
            case 3: return "已完成";
            case 4: return "已退票";
            case 5: return "已改签";
            default: return "未知";
        }
    }
}