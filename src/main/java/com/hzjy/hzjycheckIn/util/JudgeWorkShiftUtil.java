package com.hzjy.hzjycheckIn.util;

public class JudgeWorkShiftUtil {
    
    /**
     * 判断是否是普通班次
     * @param workShiftId
     * @return true:普通班次,false:加班班次
     */

    public static Boolean isCommonShift(Integer workShiftId) {
        // 1 冬令时上午；2 冬令时下午；3 冬令时加班班次；
        // 4 夏令时上午；5 夏令时下午；6 夏令时加班班次

        return workShiftId == 1 || workShiftId == 2  || workShiftId == 4 || workShiftId == 5;
    }

    // 判断当前班次是上午还是下午
    public static Boolean isAmShift(Integer workShiftId) {
        return workShiftId == 1 || workShiftId == 4;
    }
}
