package com.xuanxue.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class NameScoreResponse {
    private String name;
    private int score;
    private String summary;
    private List<String> details;
}
