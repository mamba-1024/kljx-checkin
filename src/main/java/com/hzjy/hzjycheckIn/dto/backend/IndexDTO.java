package com.hzjy.hzjycheckIn.dto.backend;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IndexDTO {


    /**
     * 一个时间日期控件
     * 1.所有目前启用员工数量（不包括待审核），停用员工数量，昨天截止24点新增员工数量，点击跳转到员工列表
     * 2.昨天员工班次1上班多少人，班次2上班多少人，点击数字跳转考勤
     * 3.截止目前员工等级分布，0星10人，1星100人，2星50人，3星50人
     * 4.员工积分排名，展示前十名
     * 5.截止当前时间待审核员工数，点击跳转到待审核
     */
    private Integer currentEnableEmployeeCount;

    private Integer currentDisableEmployeeCount;


    private Integer last24HourNewEmployee;



}
