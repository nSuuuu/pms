package com.niit.entity;

import java.util.regex.Pattern;

public class NameValidator {
    private static final Pattern CHINESE_NAME_PATTERN =
            Pattern.compile("^[\u4e00-\u9fa5]{2,4}$");

    public static boolean isValidChineseName(String name) {
        return name != null && CHINESE_NAME_PATTERN.matcher(name).matches();
    }
}