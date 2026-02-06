package com.xuanxue.dto;

import lombok.Data;

@Data
public class NameScoreRequest {
    private String name;
    private Integer year;
    private Integer month;
    private Integer day;
    private Integer hour;
    private Boolean isLunar;
}
