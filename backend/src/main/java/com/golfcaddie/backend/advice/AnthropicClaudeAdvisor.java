package com.golfcaddie.backend.advice;

import com.anthropic.client.AnthropicClient;
import com.anthropic.models.messages.Message;
import com.anthropic.models.messages.MessageCreateParams;
import com.anthropic.models.messages.TextBlockParam;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Calls Claude over HTTP via the official SDK. The static block is sent as the system
 * prompt; once it grows past the model's minimum cacheable prefix a cache_control
 * breakpoint can be added here to cut input cost.
 */
@Component
public class AnthropicClaudeAdvisor implements ClaudeAdvisor {

    private final AnthropicClient anthropicClient;
    private final ClaudeProperties properties;

    public AnthropicClaudeAdvisor(AnthropicClient anthropicClient, ClaudeProperties properties) {
        this.anthropicClient = anthropicClient;
        this.properties = properties;
    }

    @Override
    public String advise(String systemPrompt, String userPrompt) {
        try {
            MessageCreateParams params = MessageCreateParams.builder()
                    .model(properties.model())
                    .maxTokens(properties.maxTokens())
                    .systemOfTextBlockParams(List.of(
                            TextBlockParam.builder().text(systemPrompt).build()))
                    .addUserMessage(userPrompt)
                    .build();

            Message response = anthropicClient.messages().create(params);
            return response.content().stream()
                    .flatMap(block -> block.text().stream())
                    .map(textBlock -> textBlock.text())
                    .collect(Collectors.joining("\n"))
                    .strip();
        } catch (RuntimeException e) {
            throw new AdviceProviderException(
                    String.format("Claude advice call failed using model %s", properties.model()), e);
        }
    }
}
