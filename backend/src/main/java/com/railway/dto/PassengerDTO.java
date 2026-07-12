package com.railway.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.time.LocalDate;

/**
 * 添加/更新旅客DTO
 */
@Data
public class PassengerDTO {

    /** 证件类型 */
    @NotBlank(message = "证件类型不能为空")
    private String idType;

    @NotBlank(message = "姓不能为空")
    private String lastName;

    @NotBlank(message = "名不能为空")
    private String firstName;

    @NotBlank(message = "证件号不能为空")
    private String idCard;

    /** 旅客类型 1-成人 2-儿童 3-学生 4-残军 */
    private Integer passengerType;

    private String phone;

    /** 国籍（外国人永久居留身份证、外国护照需要） */
    private String nationality;

    /** 性别 1-男 2-女 */
    private Integer gender;

    /** 出生日期 */
    private LocalDate birthDate;

    /** 证件有效期截止日期 */
    private LocalDate expireDate;

    /** 电子邮箱（仅外国护照需要） */
    private String email;

    /** 学生相关信息 */
    private String studentSchool;
    private String studentNo;
    private LocalDate studentValidStart;
    private LocalDate studentValidEnd;
}
