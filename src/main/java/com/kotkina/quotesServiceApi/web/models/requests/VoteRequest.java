package com.kotkina.quotesServiceApi.web.models.requests;

import com.kotkina.quotesServiceApi.entities.AssessmentType;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VoteRequest {

    @NotBlank
    private long userId;

    @NotBlank
    private long quoteId;

    @NotBlank
    private AssessmentType assessment;
}
