package com.write.write.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "rag.service")
public class RagConfig {
    private boolean enabled = false;
    private String baseUrl = "http://localhost:8001";
    private int timeoutSeconds = 60;
}
