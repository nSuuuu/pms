package com.niit.entity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

public class IdCardValidator {
    private static final Pattern ID_CARD_PATTERN =
            Pattern.compile("^[1-9]\\d{5}(18|19|20)\\d{2}(0[1-9]|1[0-2])(0[1-9]|[12]\\d|3[01])\\d{3}[\\dXx]$");

    public static boolean isValid(String idCard) {
        if (idCard == null || idCard.length() != 18) {
            return false;
        }

        if (!ID_CARD_PATTERN.matcher(idCard).matches()) {
            return false;
        }

        // 校验码验证
        char[] idCardArray = idCard.toUpperCase().toCharArray();
        int[] coefficient = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};
        char[] checkCode = {'1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2'};

        int sum = 0;
        for (int i = 0; i < 17; i++) {
            sum += (idCardArray[i] - '0') * coefficient[i];
        }

        return checkCode[sum % 11] == idCardArray[17];
    }

    public static boolean isBirthdayMatch(String idCard, Date birthday) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            String birthDateFromId = idCard.substring(6, 14);
            Date idCardBirthday = sdf.parse(birthDateFromId);

            SimpleDateFormat dayFormat = new SimpleDateFormat("yyyyMMdd");
            String actualBirthday = dayFormat.format(birthday);

            return birthDateFromId.equals(actualBirthday);
        } catch (ParseException e) {
            return false;
        }
    }
}