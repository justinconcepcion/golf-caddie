package com.golfcaddie.backend.advice;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Claude proxy configuration. The API key is read server-side only and never reaches
 * the client. Defaults to Haiku for cost; switch the model for a quality A/B test.
 */
@ConfigurationProperties(prefix = "golfcaddie.claude")
public record ClaudeProperties(String apiKey, String model, long maxTokens) {

    public ClaudeProperties {
        if (model == null || model.isBlank()) {
            model = "claude-haiku-4-5";
        }
        if (maxTokens <= 0) {
            maxTokens = 512;
        }
        if (apiKey == null) {
            apiKey = "";
        }
    }
}
