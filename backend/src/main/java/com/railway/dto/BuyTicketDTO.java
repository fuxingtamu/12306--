package com.railway.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 购票DTO
 */
@Data
public class BuyTicketDTO {

    @NotNull(message = "车次ID不能为空")
    private Long trainId;

    @NotNull(message = "出行日期不能为空")
    private String travelDate;

    @NotNull(message = "出发站不能为空")
    private String startStation;

    @NotNull(message = "到达站不能为空")
    private String endStation;

    @NotNull(message = "座位类型不能为空")
    private Long seatTypeId;

    /** 选择的座位号列表 */
    private List<String> seatNumbers;

    @NotEmpty(message = "乘客列表不能为空")
    private List<PassengerInfo> passengers;

    /**
     * 乘客信息
     */
    @Data
    public static class PassengerInfo {
        @NotNull(message = "旅客ID不能为空")
        private Long passengerId;
        /** 座位号 */
        private String seatNumber;
    }
}