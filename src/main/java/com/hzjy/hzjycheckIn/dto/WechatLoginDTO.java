package com.hzjy.hzjycheckIn.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@ApiModel("微信登录")
@Data
public class WechatLoginDTO {

    private String code;

    private String iv;

    private String encryptedData;

}
