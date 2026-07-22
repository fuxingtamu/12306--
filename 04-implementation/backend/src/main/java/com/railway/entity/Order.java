package com.railway.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * 订单实体
 */
@Data
@TableName("orders")
public class Order implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 订单ID */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /** 订单号 */
    private String orderNo;

    /** 用户ID */
    private Long userId;

    /** 车次ID */
    private Long trainId;

    /** 车次号 */
    private String trainCode;

    /** 出行日期 */
    private LocalDate travelDate;

    /** 出发站ID */
    private Long startStationId;

    /** 出发站名称 */
    private String startStationName;

    /** 到达站ID */
    private Long endStationId;

    /** 到达站名称 */
    private String endStationName;

    /** 发车时间 */
    private LocalTime startTime;

    /** 到达时间 */
    private LocalTime endTime;

    /** 乘客数量 */
    private Integer passengerCount;

    /** 订单总价 */
    private BigDecimal totalPrice;

    /** 实付金额 */
    private BigDecimal payPrice;

    /** 支付时间 */
    private LocalDateTime payTime;

    /** 支付方式 1-微信 2-支付宝 3-银行卡 */
    private Integer payType;

    /** 订单状态 0-待支付 1-已支付 2-已取消 3-已完成 4-已退票 5-已改签 */
    private Integer orderStatus;

    /** 取消时间 */
    private LocalDateTime cancelTime;

    /** 取消原因 */
    private String cancelReason;

    /** 创建时间 */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /** 更新时间 */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}