package com.railway.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 车站实体
 */
@Data
@TableName("stations")
public class Station implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 车站ID */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /** 车站代码(电报码) */
    private String stationCode;

    /** 车站名称 */
    private String stationName;

    /** 所属城市 */
    private String cityName;

    /** 城市代码 */
    private String cityCode;

    /** 所属省份 */
    private String province;

    /** 车站拼音 */
    private String stationPinyin;

    /** 车站拼音缩写 */
    private String stationShortPinyin;

    /** 车站等级 */
    private Integer stationLevel;

    /** 是否高铁站 0-否 1-是 */
    private Integer isHighSpeed;

    /** 经度 */
    private BigDecimal longitude;

    /** 纬度 */
    private BigDecimal latitude;

    /** 状态 */
    private Integer status;

    /** 创建时间 */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}