package com.model.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
public class Work {
    private Long workId;//工作量ID

    private String userId;//学号

    private String workName;//工作量名称

    private Double hour;//时长

    private LocalDate startTime;//开始时间

    private LocalDate deadline;//ddl

    public void setHour(Double hour) {
        this.hour = (hour == null) ? null : (double) Math.round(hour * 10) / 10;
    }

    public Double getHour() {
        return this.hour;
    }

    public boolean setDeadline(String deadline) {
        try {
            this.deadline = (deadline == null) ? null : LocalDate.parse(deadline, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


}
