package com.model.dto;

/**
 * 根据总获取时长，返回的数据类型
 */
public class YearHour {
    private Integer year;
    private Double hour;

    public YearHour(Integer year) {
        this.year = year;
        this.hour = 0.0;
    }

    //添加时长
    public void addHour(Double hour) {
        this.hour += hour;
    }
}
