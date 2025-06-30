package com.niit.utils;

public class IDCardUtil {
    // 简单的身份证号验证
    public static boolean validateIDCard(String idCard) {
        if (idCard == null || idCard.length() != 18) {
            return false;
        }

        // 验证前17位是否为数字
        for (int i = 0; i < 17; i++) {
            if (!Character.isDigit(idCard.charAt(i))) {
                return false;
            }
        }

        // 验证最后一位
        char lastChar = idCard.charAt(17);
        return Character.isDigit(lastChar) || lastChar == 'X' || lastChar == 'x';
    }

    // 从身份证号获取籍贯信息（简化版）
    public static String getNativePlaceFromID(String idCard) {
        if (!validateIDCard(idCard)) {
            return "";
        }

        String areaCode = idCard.substring(0, 6);
        // 这里可以添加更详细的地区码映射
        if (areaCode.startsWith("11")) return "北京";
        if (areaCode.startsWith("31")) return "上海";
        if (areaCode.startsWith("33")) return "浙江";
        if (areaCode.startsWith("44")) return "广东";
        // 添加更多省份代码...

        return "";
    }
}