package com.railway.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalTime;

/**
 * 车次经停站实体
 */
@Data
@TableName("train_stops")
public class TrainStop implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long trainId;
    private String trainCode;
    private Long stationId;
    private String stationName;
    private Integer stopOrder;
    private LocalTime arriveTime;
    private LocalTime departTime;
    private Integer stopDuration;
    private Integer distance;
    private Integer isStart;
    private Integer isEnd;
    private Integer status;
}