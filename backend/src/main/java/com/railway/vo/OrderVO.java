package com.railway.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

/**
 * 订单VO
 */
@Data
public class OrderVO {

    private Long orderId;
    private String orderNo;
    private String trainCode;
    private LocalDate travelDate;
    private String startStationName;
    private String endStationName;
    private LocalTime startTime;
    private LocalTime endTime;
    private Integer passengerCount;
    private BigDecimal totalPrice;
    private BigDecimal payPrice;
    private Integer orderStatus;
    private String orderStatusName;
    private LocalDateTime createTime;
    private LocalDateTime payTime;

    private List<OrderItemVO> items;

    /**
     * 订单明细VO
     */
    @Data
    public static class OrderItemVO {
        private Long itemId;
        private String passengerName;
        private String passengerIdCard;
        private String seatTypeName;
        private String seatNumber;
        private Integer carriageNumber;
        private BigDecimal price;
        private String ticketNo;
        private Integer ticketStatus;
    }
}