package com.kotkina.quotesServiceApi.web.models.requests;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class QuoteRequest {

    @NotBlank
    private String content;
}
