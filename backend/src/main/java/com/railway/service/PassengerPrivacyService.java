package com.railway.service;

import com.railway.common.IdType;
import org.springframework.stereotype.Service;

/**
 * 乘车人隐私保护服务
 * 集中管理证件号和手机号的脱敏规则
 */
@Service
public class PassengerPrivacyService {

    /**
     * 对证件号进行脱敏。
     * 规则：
     * 1. 如果证件号为空或空白字符串，返回空字符串。
     * 2. 如果长度 <= 4，返回 "****"。
     * 3. 如果长度 <= 8，保留前2位和后2位，中间用 **** 代替。
     *    示例："12345678" → "12****78"
     * 4. 如果长度 > 8，保留前4位和后4位，中间用 ******** 代替。
     *    示例："110101199001011234" → "1101********1234"
     *
     * @param idCardNo 原始证件号码
     * @return 脱敏后的证件号码
     */
    public String maskIdCardNo(String idCardNo) {
        if (idCardNo == null || idCardNo.isBlank()) {
            return "";
        }

        int length = idCardNo.length();

        if (length <= 4) {
            return "****";
        }

        if (length <= 8) {
            return idCardNo.substring(0, 2)
                    + "****"
                    + idCardNo.substring(length - 2);
        }

        return idCardNo.substring(0, 4)
                + "********"
                + idCardNo.substring(length - 4);
    }

    /**
     * 根据证件类型进行差异化脱敏。
     * 身份证：保留前4后4
     * 护照类：保留前2后2
     * 其他：通用规则
     *
     * @param idType   证件类型
     * @param idCardNo 原始证件号码
     * @return 脱敏后的证件号码
     */
    public String maskIdCardNo(String idType, String idCardNo) {
        if (idCardNo == null || idCardNo.isBlank()) {
            return "";
        }

        int length = idCardNo.length();

        if (length <= 4) {
            return "****";
        }

        switch (idType) {
            case IdType.ID_CARD:
                if (length <= 8) {
                    return idCardNo.substring(0, 2) + "****" + idCardNo.substring(length - 2);
                }
                return idCardNo.substring(0, 4) + "********" + idCardNo.substring(length - 4);

            case IdType.CHINA_PASSPORT:
            case IdType.FOREIGN_PASSPORT:
                if (length <= 6) {
                    return idCardNo.substring(0, 1) + "****" + idCardNo.substring(length - 1);
                }
                return idCardNo.substring(0, 2) + "****" + idCardNo.substring(length - 2);

            case IdType.HK_MACAO_PASS:
            case IdType.TAIWAN_PASS:
                return idCardNo.substring(0, 2) + "******" + idCardNo.substring(length - 2);

            default:
                if (length <= 8) {
                    return idCardNo.substring(0, 2) + "****" + idCardNo.substring(length - 2);
                }
                return idCardNo.substring(0, 4) + "********" + idCardNo.substring(length - 4);
        }
    }

    /**
     * 对手机号进行脱敏。
     * 规则：
     * 1. null或空白 → 返回空字符串
     * 2. 11位手机号 → "138****8000" (保留前3后4)
     * 3. 非11位 → 仅保留首尾各1位，中间全星号
     *
     * @param phone 原始手机号
     * @return 脱敏后的手机号
     */
    public String maskPhone(String phone) {
        if (phone == null || phone.isBlank()) {
            return "";
        }

        int length = phone.length();

        if (length == 11) {
            return phone.substring(0, 3)
                    + "****"
                    + phone.substring(7);
        }

        return phone.charAt(0)
                + "*".repeat(Math.max(0, length - 2))
                + phone.charAt(length - 1);
    }
}