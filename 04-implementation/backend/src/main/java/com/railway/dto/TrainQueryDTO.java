package com.railway.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

/**
 * 车次查询DTO
 */
@Data
public class TrainQueryDTO {

    @NotBlank(message = "出发站不能为空")
    private String startStation;

    @NotBlank(message = "到达站不能为空")
    private String endStation;

    @NotNull(message = "出行日期不能为空")
    private LocalDate travelDate;

    /** 车次类型筛选 */
    private Integer trainType;

    /** 是否只看高铁 */
    private Boolean onlyHighSpeed;

    /** 排序方式 1-时间 2-价格 3-耗时 */
    private Integer sortType;
}