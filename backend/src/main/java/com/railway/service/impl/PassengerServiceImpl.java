package com.railway.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.railway.common.IdType;
import com.railway.dto.PassengerDTO;
import com.railway.entity.Passenger;
import com.railway.mapper.PassengerMapper;
import com.railway.service.PassengerService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 旅客服务实现
 */
@Service
public class PassengerServiceImpl extends ServiceImpl<PassengerMapper, Passenger> implements PassengerService {

    /** 旅客最大数量 */
    private static final int MAX_PASSENGER_COUNT = 30;

    /** 删除冷却期天数 */
    private static final int DELETE_COOLDOWN_DAYS = 30;

    /** 身份证号校验正则 */
    private static final Pattern ID_CARD_PATTERN = Pattern.compile("^[1-9]\\d{5}(18|19|20)\\d{2}(0[1-9]|1[0-2])(0[1-9]|[12]\\d|3[01])\\d{3}[0-9Xx]$");

    /** 港澳通行证号校验正则 (H/T + 8位数字) */
    private static final Pattern HK_MACAO_PASS_PATTERN = Pattern.compile("^[HHT]\\d{8}$");

    /** 台湾通行证号校验正则 (W + 8位数字) */
    private static final Pattern TAIWAN_PASS_PATTERN = Pattern.compile("^W\\d{8}$");

    /** 中国护照号校验正则 (E + 8位数字) */
    private static final Pattern CHINA_PASSPORT_PATTERN = Pattern.compile("^E\\d{8}$");

    /** 外国人永久居留证号校验正则 (字母开头 + 数字) */
    private static final Pattern FOREIGN_PERMANENT_PATTERN = Pattern.compile("^[A-Za-z]\\d+$");

    /** 外国护照号校验正则 (字母开头 + 数字) */
    private static final Pattern FOREIGN_PASSPORT_PATTERN = Pattern.compile("^[A-Za-z]\\d+$");

    @Override
    public List<Passenger> getPassengerList(Long userId) {
        return this.lambdaQuery()
                .eq(Passenger::getUserId, userId)
                .eq(Passenger::getStatus, 1)
                .orderByDesc(Passenger::getIsDefault)
                .orderByDesc(Passenger::getCreateTime)
                .list();
    }

    @Override
    @Transactional
    public Passenger addPassenger(Long userId, PassengerDTO passengerDTO) {
        // 校验证件类型
        String idType = passengerDTO.getIdType();
        if (idType == null || idType.isEmpty()) {
            throw new RuntimeException("证件类型不能为空");
        }

        // 校验证件号格式
        validateIdCard(idType, passengerDTO.getIdCard());

        // 检查证件号是否已存在（同一用户同一证件类型）
        Passenger existPassenger = this.lambdaQuery()
                .eq(Passenger::getUserId, userId)
                .eq(Passenger::getIdType, idType)
                .eq(Passenger::getIdCard, passengerDTO.getIdCard())
                .eq(Passenger::getStatus, 1)
                .one();

        if (existPassenger != null) {
            throw new RuntimeException("该证件号已添加为旅客");
        }

        // 检查旅客数量限制（最多30人）
        long count = this.lambdaQuery()
                .eq(Passenger::getUserId, userId)
                .eq(Passenger::getStatus, 1)
                .count();

        if (count >= MAX_PASSENGER_COUNT) {
            throw new RuntimeException("每个账号最多允许添加30个乘客");
        }

        // 如果是第一个旅客，设为默认
        boolean isFirst = count == 0;

        // 创建旅客
        Passenger passenger = new Passenger();
        passenger.setUserId(userId);
        passenger.setIdType(idType);
        passenger.setName(passengerDTO.getName());
        passenger.setIdCard(passengerDTO.getIdCard());
        passenger.setPassengerType(passengerDTO.getPassengerType() != null ? passengerDTO.getPassengerType() : 1);
        passenger.setPhone(passengerDTO.getPhone());
        passenger.setNationality(passengerDTO.getNationality());
        passenger.setEmail(passengerDTO.getEmail());
        passenger.setIsDefault(isFirst ? 1 : 0);

        // 设置性别和出生日期
        if (IdType.ID_CARD.equals(idType)) {
            // 身份证可以自动提取
            if (passengerDTO.getGender() == null) {
                passenger.setGender(getGenderFromIdCard(passengerDTO.getIdCard()));
            } else {
                passenger.setGender(passengerDTO.getGender());
            }
            if (passengerDTO.getBirthDate() == null) {
                passenger.setBirthDate(getBirthDateFromIdCard(passengerDTO.getIdCard()));
            } else {
                passenger.setBirthDate(passengerDTO.getBirthDate());
            }
        } else {
            // 其他证件类型使用填写值
            passenger.setGender(passengerDTO.getGender());
            passenger.setBirthDate(passengerDTO.getBirthDate());
        }

        passenger.setExpireDate(passengerDTO.getExpireDate());

        // 学生信息
        passenger.setStudentSchool(passengerDTO.getStudentSchool());
        passenger.setStudentNo(passengerDTO.getStudentNo());
        passenger.setStudentValidStart(passengerDTO.getStudentValidStart());
        passenger.setStudentValidEnd(passengerDTO.getStudentValidEnd());

        passenger.setIsVerified(0);
        passenger.setStatus(1);
        passenger.setCreateTime(LocalDateTime.now());
        passenger.setUpdateTime(LocalDateTime.now());

        this.save(passenger);
        return passenger;
    }

    @Override
    @Transactional
    public Passenger updatePassenger(Long userId, Long passengerId, PassengerDTO passengerDTO) {
        Passenger passenger = this.lambdaQuery()
                .eq(Passenger::getId, passengerId)
                .eq(Passenger::getUserId, userId)
                .eq(Passenger::getStatus, 1)
                .one();

        if (passenger == null) {
            throw new RuntimeException("旅客不存在");
        }

        // 更新旅客信息（证件类型和证件号不可修改）
        passenger.setName(passengerDTO.getName());
        passenger.setPassengerType(passengerDTO.getPassengerType());
        passenger.setPhone(passengerDTO.getPhone());
        passenger.setNationality(passengerDTO.getNationality());
        passenger.setEmail(passengerDTO.getEmail());
        passenger.setGender(passengerDTO.getGender());
        passenger.setBirthDate(passengerDTO.getBirthDate());
        passenger.setExpireDate(passengerDTO.getExpireDate());
        passenger.setStudentSchool(passengerDTO.getStudentSchool());
        passenger.setStudentNo(passengerDTO.getStudentNo());
        passenger.setStudentValidStart(passengerDTO.getStudentValidStart());
        passenger.setStudentValidEnd(passengerDTO.getStudentValidEnd());
        passenger.setUpdateTime(LocalDateTime.now());

        this.updateById(passenger);
        return passenger;
    }

    @Override
    @Transactional
    public void deletePassenger(Long userId, Long passengerId) {
        Passenger passenger = this.lambdaQuery()
                .eq(Passenger::getId, passengerId)
                .eq(Passenger::getUserId, userId)
                .eq(Passenger::getStatus, 1)
                .one();

        if (passenger == null) {
            throw new RuntimeException("旅客不存在");
        }

        // 检查是否在删除冷却期内（添加后30天内不可删除）
        if (passenger.getCreateTime() != null) {
            long daysSinceCreation = ChronoUnit.DAYS.between(passenger.getCreateTime(), LocalDateTime.now());
            if (daysSinceCreation < DELETE_COOLDOWN_DAYS) {
                throw new RuntimeException("乘车人在添加后30天内不允许删除");
            }
        }

        passenger.setStatus(0);
        this.updateById(passenger);
    }

    @Override
    public Passenger getPassengerDetail(Long userId, Long passengerId) {
        Passenger passenger = this.lambdaQuery()
                .eq(Passenger::getId, passengerId)
                .eq(Passenger::getUserId, userId)
                .eq(Passenger::getStatus, 1)
                .one();

        if (passenger == null) {
            throw new RuntimeException("旅客不存在");
        }

        return passenger;
    }

    @Override
    @Transactional
    public void setDefaultPassenger(Long userId, Long passengerId) {
        Passenger passenger = this.lambdaQuery()
                .eq(Passenger::getId, passengerId)
                .eq(Passenger::getUserId, userId)
                .eq(Passenger::getStatus, 1)
                .one();

        if (passenger == null) {
            throw new RuntimeException("旅客不存在");
        }

        // 取消该用户的所有默认标记
        this.lambdaUpdate()
                .eq(Passenger::getUserId, userId)
                .eq(Passenger::getIsDefault, 1)
                .set(Passenger::getIsDefault, 0)
                .update();

        // 设置新的默认旅客
        passenger.setIsDefault(1);
        passenger.setUpdateTime(LocalDateTime.now());
        this.updateById(passenger);
    }

    /**
     * 校验证件号格式
     */
    private void validateIdCard(String idType, String idCard) {
        if (idCard == null || idCard.isEmpty()) {
            throw new RuntimeException("证件号不能为空");
        }

        boolean valid = false;
        switch (idType) {
            case IdType.ID_CARD:
                valid = ID_CARD_PATTERN.matcher(idCard).matches();
                if (!valid) {
                    throw new RuntimeException("身份证号格式不正确，应为18位");
                }
                break;

            case IdType.HK_MACAO_PASS:
                valid = HK_MACAO_PASS_PATTERN.matcher(idCard).matches();
                if (!valid) {
                    throw new RuntimeException("港澳居民来往内地通行证号格式不正确");
                }
                break;

            case IdType.TAIWAN_PASS:
                valid = TAIWAN_PASS_PATTERN.matcher(idCard).matches();
                if (!valid) {
                    throw new RuntimeException("台湾居民来往大陆通行证号格式不正确");
                }
                break;

            case IdType.CHINA_PASSPORT:
                valid = CHINA_PASSPORT_PATTERN.matcher(idCard).matches();
                if (!valid) {
                    throw new RuntimeException("中国护照号格式不正确，应以E开头后跟8位数字");
                }
                break;

            case IdType.FOREIGN_PERMANENT:
            case IdType.FOREIGN_PASSPORT:
                valid = FOREIGN_PERMANENT_PATTERN.matcher(idCard).matches();
                if (!valid) {
                    throw new RuntimeException("证件号格式不正确，应为字母开头后跟数字");
                }
                break;

            case IdType.HK_MACAO_RESIDENCE:
            case IdType.TAIWAN_RESIDENCE:
                // 居住证暂无固定格式校验
                if (idCard.length() < 6) {
                    throw new RuntimeException("证件号格式不正确");
                }
                valid = true;
                break;

            default:
                throw new RuntimeException("不支持的证件类型: " + idType);
        }
    }

    /**
     * 从身份证号获取性别
     */
    private Integer getGenderFromIdCard(String idCard) {
        if (idCard == null || idCard.length() < 17) {
            return 1;
        }
        int genderCode = Integer.parseInt(idCard.substring(16, 17));
        return genderCode % 2 == 1 ? 1 : 2;
    }

    /**
     * 从身份证号获取出生日期
     */
    private LocalDate getBirthDateFromIdCard(String idCard) {
        if (idCard == null || idCard.length() < 14) {
            return null;
        }
        String birthStr = idCard.substring(6, 14);
        try {
            int year = Integer.parseInt(birthStr.substring(0, 4));
            int month = Integer.parseInt(birthStr.substring(4, 6));
            int day = Integer.parseInt(birthStr.substring(6, 8));
            return LocalDate.of(year, month, day);
        } catch (Exception e) {
            return null;
        }
    }
}
