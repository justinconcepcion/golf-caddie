package com.golfcaddie.backend.advice;

import com.golfcaddie.backend.advice.dto.AdviceRequest;
import com.golfcaddie.backend.advice.dto.AdviceResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** Per-shot caddie advice, assembled server-side and proxied to Claude. */
@RestController
@RequestMapping("/api/v1/advice")
public class AdviceController {

    private final AdviceService adviceService;

    public AdviceController(AdviceService adviceService) {
        this.adviceService = adviceService;
    }

    @PostMapping
    public ResponseEntity<AdviceResponse> getAdvice(@Valid @RequestBody AdviceRequest request) {
        return ResponseEntity.ok(adviceService.getAdvice(request));
    }
}
