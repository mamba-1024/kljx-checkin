package com.hzjy.hzjycheckIn.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class BigDecimalGetHoursUtil {



    public static BigDecimal minutesToHours(long minutes){

        BigDecimal result = new BigDecimal(minutes/60.0).setScale(1, RoundingMode.HALF_UP);
        return result;

    }


}
