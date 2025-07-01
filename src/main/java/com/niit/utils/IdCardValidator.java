package com.niit.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class IdCardValidator {
    public static boolean isValid(String idCard) {
        if (idCard == null || idCard.length() != 18) {
            return false;
        }

        // 校验格式（不含最后一位）
        String regex = "^[1-9]\\d{5}(18|19|20)\\d{2}(0[1-9]|1[0-2])(0[1-9]|[12]\\d|3[01])\\d{3}[\\dXx]$";
        if (!idCard.matches(regex)) {
            return false;
        }

        // 提取出生日期并验证是否合法
        String birthStr = idCard.substring(6, 14);
        try {
            LocalDate birthDate = LocalDate.parse(birthStr, DateTimeFormatter.BASIC_ISO_DATE);
            if (birthDate.isAfter(LocalDate.now())) {
                return false; // 出生日期不能在未来
            }
        } catch (Exception e) {
            return false;
        }

        // ✅ 不再进行第18位校验码验证

        return true;
    }

    public static String getGenderFromIdCard(String idCard) {
        if (idCard == null || idCard.length() != 18) {
            return "";
        }
        int genderDigit = Character.getNumericValue(idCard.charAt(16));
        return genderDigit % 2 == 1 ? "男" : "女";
    }

    /**
     * 从身份证号码中提取出生日期字符串
     */
    public static String extractBirthday(String idCard) {
        if (idCard == null || idCard.length() != 18) return null;
        String birthStr = idCard.substring(6, 14); // 从第7位开始取8位作为出生日期
        return birthStr; // 格式：yyyyMMdd
    }

    /**
     * 返回格式化的出生日期 yyyy-MM-dd
     */
    public static String getFormattedBirthday(String idCard) {
        String birthStr = extractBirthday(idCard);
        if (birthStr == null) return null;
        try {
            LocalDate birthDate = LocalDate.parse(birthStr, DateTimeFormatter.BASIC_ISO_DATE);
            return birthDate.format(DateTimeFormatter.ISO_LOCAL_DATE); // yyyy-MM-dd
        } catch (Exception e) {
            return null;
        }
    }
}