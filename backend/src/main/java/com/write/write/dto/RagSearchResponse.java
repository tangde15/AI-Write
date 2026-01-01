package com.write.write.dto;

import com.write.write.model.EssayVectorResult;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RagSearchResponse {
    private boolean success;
    private List<EssayVectorResult> results;
    private String message;
}
