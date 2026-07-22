package com.railway.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 乘车人隐私脱敏服务 - 单元测试
 * 对应测试用例: TC-PA-08, TC-PA-09
 */
@DisplayName("PassengerPrivacyService")
class PassengerPrivacyServiceTest {

    private final PassengerPrivacyService service = new PassengerPrivacyService();

    @Nested
    @DisplayName("证件号脱敏 maskIdCardNo")
    class IdCardMasking {

        @Test
        @DisplayName("null返回空字符串")
        void shouldReturnEmptyForNull() {
            assertEquals("", service.maskIdCardNo(null));
        }

        @Test
        @DisplayName("空字符串返回空字符串")
        void shouldReturnEmptyForBlank() {
            assertEquals("", service.maskIdCardNo(""));
        }

        @ParameterizedTest
        @CsvSource({
            "'123',      '****'",
            "'1234',     '****'",
            "'12345678', '12****78'",
            "'110101199001011234', '1101********1234'"
        })
        @DisplayName("各长度区间的证件号脱敏格式")
        void shouldMaskByIdCardLength(String input, String expected) {
            assertEquals(expected, service.maskIdCardNo(input));
        }
    }

    @Nested
    @DisplayName("手机号脱敏 maskPhone")
    class PhoneMasking {

        @Test
        @DisplayName("null返回空字符串")
        void shouldReturnEmptyForNull() {
            assertEquals("", service.maskPhone(null));
        }

        @Test
        @DisplayName("11位标准手机号: 保留前3后4")
        void shouldMaskStandardPhone() {
            assertEquals("138****8000", service.maskPhone("13800138000"));
        }

        @Test
        @DisplayName("非11位号码: 保留首尾各1位")
        void shouldMaskNonStandardPhone() {
            String result = service.maskPhone("1234567890");
            assertEquals("1", result.substring(0, 1));
            assertEquals("0", result.substring(result.length() - 1));
            assertTrue(result.contains("*"));
        }
    }
}
