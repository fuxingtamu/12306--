package com.railway.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * 车次实体
 */
@Data
@TableName("trains")
public class Train implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 车次ID */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /** 车次号 */
    private String trainCode;

    /** 车次类型 1-高铁(G) 2-动车(D) 3-城际(C) 4-直达(Z) 5-特快(T) 6-快速(K) 7-普快 */
    private Integer trainType;

    /** 始发站ID */
    private Long startStationId;

    /** 终点站ID */
    private Long endStationId;

    /** 始发站名称 */
    private String startStationName;

    /** 终点站名称 */
    private String endStationName;

    /** 始发时间 */
    private LocalTime startTime;

    /** 终到时间 */
    private LocalTime endTime;

    /** 总耗时(分钟) */
    private Integer totalTime;

    /** 总里程(公里) */
    private Integer totalDistance;

    /** 总停靠站数 */
    private Integer totalStops;

    /** 是否支持选座 0-不支持 1-支持 */
    private Integer hasSeatSelection;

    /** 状态 */
    private Integer status;

    /** 创建时间 */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}