package com.xuanxue.dto;

import lombok.Data;

@Data
public class NamingRequest {
    private String surname;
    private Integer gender;
    private Integer year;
    private Integer month;
    private Integer day;
    private Integer hour;
    private Boolean isLunar;
    private Integer count;
}
