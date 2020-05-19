package com.model.dto;

/**
 * 根据年获取时长，返回的数据类型
 */
public class MonthHour {
    private Integer month;
    private Double hour;

    public MonthHour(Integer month) {
        this.month = month;
        this.hour = 0.0;
    }

    //添加时长
    public void addHour(Double hour) {
        this.hour += hour;
    }
}
