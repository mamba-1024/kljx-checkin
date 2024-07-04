package com.hzjy.hzjycheckIn.config;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ExcelData {

    private String fileName;

    private String[] head;

    private List<String[]> data;

}
