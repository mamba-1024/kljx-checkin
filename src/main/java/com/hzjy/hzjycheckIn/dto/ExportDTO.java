package com.hzjy.hzjycheckIn.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@ApiModel(description = "导出")
@Data
public class ExportDTO {

    private String exportUrl;

    private String fileName;

}
