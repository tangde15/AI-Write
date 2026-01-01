package com.write.write.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RagFeedbackResponse {
    private boolean success;
    private String feedback;
    private String message;
}
