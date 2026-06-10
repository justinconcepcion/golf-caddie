package com.golfcaddie.backend.advice;

/** Boundary over the LLM provider so advice logic stays testable without the SDK. */
public interface ClaudeAdvisor {

    /**
     * @param systemPrompt stable instructions + player/hole context (cacheable once large enough)
     * @param userPrompt   the current shot situation
     * @return the caddie's recommendation text
     */
    String advise(String systemPrompt, String userPrompt);
}
