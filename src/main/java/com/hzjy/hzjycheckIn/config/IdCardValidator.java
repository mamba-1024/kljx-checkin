package com.hzjy.hzjycheckIn.config;

import com.hzjy.hzjycheckIn.common.IdCard;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class IdCardValidator implements ConstraintValidator<IdCard, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.length() != 18) {
            return false;
        }

        // 正则表达式
        String regex = "^[1-9]\\d{5}(19|20)\\d{2}(0[1-9]|1[0-2])(0[1-9]|[1-2]\\d|3[0-1])\\d{3}[0-9Xx]$";

        if (!value.matches(regex)) {
            return false;
        }

        // 校验码校验
        int[] weight = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};
        char[] checkCode = {'1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2'};
        int sum = 0;
        for (int i = 0; i < 17; i++) {
            sum += (value.charAt(i) - '0') * weight[i];
        }
        int mod = sum % 11;
        char lastChar = value.charAt(17);
        char checkChar = checkCode[mod];
        return lastChar == checkChar;
    }
}

