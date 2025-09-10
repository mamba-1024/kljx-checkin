package com.hzjy.hzjycheckIn.config;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class FormatUtils {


    public static String time(LocalDateTime localDateTime) {
        if (Objects.isNull(localDateTime)) {
            return "";
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return localDateTime.format(formatter);
    }

    public static String time(LocalTime localTime) {
        if (Objects.isNull(localTime)) {
            return "";
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        return localTime.format(formatter);
    }
}
