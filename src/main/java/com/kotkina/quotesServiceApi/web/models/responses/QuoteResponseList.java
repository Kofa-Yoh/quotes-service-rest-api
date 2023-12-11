package com.kotkina.quotesServiceApi.web.models.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuoteResponseList {

    List<QuoteResponse> quotes = new ArrayList<>();
}
