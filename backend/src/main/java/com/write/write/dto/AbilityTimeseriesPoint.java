package com.write.write.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AbilityTimeseriesPoint {
    private Long recordId;
    private String date; // ISO string
    private String topic;
    private Integer score;
    private Integer content;
    private Integer structure;
    private Integer language;
    private Integer creativity;
}
