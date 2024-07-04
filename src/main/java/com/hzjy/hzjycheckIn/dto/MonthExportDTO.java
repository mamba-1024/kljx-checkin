package com.hzjy.hzjycheckIn.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
public class MonthExportDTO {

    private String month;

    private LocalDate monthDate;


    public LocalDate getMonthDate() {
        return LocalDate.parse(month + "-01", DateTimeFormatter.ISO_LOCAL_DATE);
    }

}
