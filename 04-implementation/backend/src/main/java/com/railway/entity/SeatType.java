package com.railway.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 座位类型实体
 */
@Data
@TableName("seat_types")
public class SeatType implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String seatTypeCode;
    private String seatTypeName;
    private String seatTypeDesc;
    private BigDecimal basePrice;
    private BigDecimal discountRate;
    private Integer hasSeatNumber;
    private Integer status;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}