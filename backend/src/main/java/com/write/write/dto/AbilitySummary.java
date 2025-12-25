package com.write.write.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AbilitySummary {
    private int totalEssays;
    private double avgScore;
    private double avgContent;
    private double avgStructure;
    private double avgLanguage;
    private double avgCreativity;
}
