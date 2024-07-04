package com.hzjy.hzjycheckIn.util;

public class JudgeWorkShiftUtil {


    public static Boolean isCommonShift(Integer workShiftId) {
        return workShiftId == 1 || workShiftId == 3;
    }

}
