package com.kotkina.quotesServiceApi.web.models.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class QuoteResponse extends RepresentationModel<QuoteResponse> {

    private long id;

    private String content;

    private long score;
}
