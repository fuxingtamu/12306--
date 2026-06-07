package com.railway.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * 车次信息VO
 */
@Data
public class TrainVO {

    /** 车次ID */
    private Long trainId;

    /** 车次号 */
    private String trainCode;

    /** 车次类型 */
    private Integer trainType;

    /** 车次类型名称 */
    private String trainTypeName;

    /** 始发站名称 */
    private String startStationName;

    /** 终点站名称 */
    private String endStationName;

    /** 发车时间 */
    private LocalTime startTime;

    /** 到达时间 */
    private LocalTime endTime;

    /** 总耗时(分钟) */
    private Integer totalTime;

    /** 总耗时文本 */
    private String totalTimeText;

    /** 出行日期 */
    private LocalDate travelDate;

    /** 是否支持选座 */
    private Boolean hasSeatSelection;

    /** 座位类型列表 */
    private List<SeatVO> seats;

    /** 经停站列表 */
    private List<StopVO> stops;

    /**
     * 座位信息VO
     */
    @Data
    public static class SeatVO {
        /** 座位类型ID */
        private Long seatTypeId;
        /** 座位类型代码 */
        private String seatTypeCode;
        /** 座位类型名称 */
        private String seatTypeName;
        /** 总座位数 */
        private Integer totalSeats;
        /** 可售座位数 */
        private Integer availableSeats;
        /** 已售座位数 */
        private Integer soldSeats;
        /** 票价 */
        private BigDecimal price;
        /** 是否有票 */
        private Boolean hasTicket;
        /** 是否可候补 */
        private Boolean canWaiting;
    }

    /**
     * 经停站信息VO
     */
    @Data
    public static class StopVO {
        /** 车站名称 */
        private String stationName;
        /** 停靠顺序 */
        private Integer stopOrder;
        /** 到达时间 */
        private LocalTime arriveTime;
        /** 发车时间 */
        private LocalTime departTime;
        /** 停靠时长(分钟) */
        private Integer stopDuration;
        /** 是否始发站 */
        private Boolean isStart;
        /** 是否终点站 */
        private Boolean isEnd;
    }
}