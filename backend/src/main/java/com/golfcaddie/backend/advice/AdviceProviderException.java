package com.golfcaddie.backend.advice;

/** Thrown when the upstream caddie-advice provider (Claude) fails. Maps to 502. */
public class AdviceProviderException extends RuntimeException {

    public AdviceProviderException(String message, Throwable cause) {
        super(message, cause);
    }
}
