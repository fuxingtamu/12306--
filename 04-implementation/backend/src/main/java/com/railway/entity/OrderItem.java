package com.railway.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单明细实体
 */
@Data
@TableName("order_items")
public class OrderItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long orderId;
    private String orderNo;
    private Long passengerId;
    private String passengerName;
    private String passengerIdCard;
    private Integer passengerType;
    private Long seatTypeId;
    private String seatTypeName;
    private String seatNumber;
    private Integer carriageNumber;
    private BigDecimal price;
    private String ticketNo;
    private Integer ticketStatus;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}