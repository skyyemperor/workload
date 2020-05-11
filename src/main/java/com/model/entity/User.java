package com.model.entity;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

@Data
public class User {
    private Integer id;//用户在数据库中的主键id

    private String userId;//学号

    private String name;//姓名

    private String campus;//校区

    private String academy;//学院

    private String department;//部门

    private String position;//职位

    private Integer flag;//是否申岗，1为是，0为否

}
