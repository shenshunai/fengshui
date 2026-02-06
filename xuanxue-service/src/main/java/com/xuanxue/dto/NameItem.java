package com.xuanxue.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NameItem {
    private String name;
    private int score;
    private String analysis;
}
