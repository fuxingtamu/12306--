package com.railway.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 旅客实体
 */
@Data
@TableName("passengers")
public class Passenger implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 旅客ID */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /** 所属用户ID */
    private Long userId;

    /** 姓 */
    private String lastName;

    /** 名 */
    private String firstName;

    /** 证件类型 */
    private String idType;

    /** 证件号码 */
    private String idCard;

    /** 旅客类型 1-成人 2-儿童 3-学生 4-残军 */
    private Integer passengerType;

    /** 手机号 */
    private String phone;

    /** 国籍（仅外国人永久居留身份证、外国护照需要） */
    private String nationality;

    /** 性别 1-男 2-女 */
    private Integer gender;

    /** 出生日期 */
    private LocalDate birthDate;

    /** 证件有效期截止日期（仅复杂证件类型需要） */
    private LocalDate expireDate;

    /** 电子邮箱（仅外国护照需要） */
    private String email;

    /** 学生学校 */
    private String studentSchool;

    /** 学生学号 */
    private String studentNo;

    /** 学生优惠有效期开始 */
    private LocalDate studentValidStart;

    /** 学生优惠有效期结束 */
    private LocalDate studentValidEnd;

    /** 是否默认旅客 0-否 1-是 */
    private Integer isDefault;

    /** 是否核验 0-未核验 1-已核验 */
    private Integer isVerified;

    /** 核验时间 */
    private LocalDateTime verifyTime;

    /** 状态 0-删除 1-正常 */
    @TableLogic
    private Integer status;

    /** 创建时间 */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /** 更新时间 */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
