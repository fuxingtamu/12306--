package com.railway.common;

/**
 * 证件类型枚举
 */
public class IdType {

    /** 中国居民身份证 */
    public static final String ID_CARD = "ID_CARD";

    /** 港澳居民居住证 */
    public static final String HK_MACAO_RESIDENCE = "HK_MACAO_RESIDENCE";

    /** 台湾居民居住证 */
    public static final String TAIWAN_RESIDENCE = "TAIWAN_RESIDENCE";

    /** 外国人永久居留身份证 */
    public static final String FOREIGN_PERMANENT = "FOREIGN_PERMANENT";

    /** 港澳居民来往内地通行证 */
    public static final String HK_MACAO_PASS = "HK_MACAO_PASS";

    /** 台湾居民来往大陆通行证 */
    public static final String TAIWAN_PASS = "TAIWAN_PASS";

    /** 中国护照 */
    public static final String CHINA_PASSPORT = "CHINA_PASSPORT";

    /** 外国护照 */
    public static final String FOREIGN_PASSPORT = "FOREIGN_PASSPORT";

    /**
     * 复杂证件类型列表（需要额外字段：国籍、出生日期、性别、有效期）
     */
    public static final String[] COMPLEX_TYPES = {
            FOREIGN_PERMANENT,
            HK_MACAO_PASS,
            TAIWAN_PASS,
            CHINA_PASSPORT,
            FOREIGN_PASSPORT
    };

    /**
     * 需要国籍的证件类型
     */
    public static final String[] NEED_NATIONALITY = {
            FOREIGN_PERMANENT,
            FOREIGN_PASSPORT
    };

    /**
     * 需要邮箱的证件类型
     */
    public static final String[] NEED_EMAIL = {
            FOREIGN_PASSPORT
    };

    /**
     * 检查是否为复杂证件类型
     */
    public static boolean isComplexType(String idType) {
        if (idType == null) return false;
        for (String type : COMPLEX_TYPES) {
            if (type.equals(idType)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 检查是否需要国籍
     */
    public static boolean needsNationality(String idType) {
        if (idType == null) return false;
        for (String type : NEED_NATIONALITY) {
            if (type.equals(idType)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 检查是否需要邮箱
     */
    public static boolean needsEmail(String idType) {
        if (idType == null) return false;
        for (String type : NEED_EMAIL) {
            if (type.equals(idType)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取证件类型中文名称
     */
    public static String getName(String idType) {
        if (idType == null) return "未知";
        switch (idType) {
            case ID_CARD: return "中国居民身份证";
            case HK_MACAO_RESIDENCE: return "港澳居民居住证";
            case TAIWAN_RESIDENCE: return "台湾居民居住证";
            case FOREIGN_PERMANENT: return "外国人永久居留身份证";
            case HK_MACAO_PASS: return "港澳居民来往内地通行证";
            case TAIWAN_PASS: return "台湾居民来往大陆通行证";
            case CHINA_PASSPORT: return "中国护照";
            case FOREIGN_PASSPORT: return "外国护照";
            default: return "未知";
        }
    }
}
