package com.kotkina.quotesServiceApi.utils.mappers;

import com.kotkina.quotesServiceApi.entities.Quote;
import com.kotkina.quotesServiceApi.entities.User;
import com.kotkina.quotesServiceApi.entities.Vote;
import com.kotkina.quotesServiceApi.web.controllers.UserController;
import com.kotkina.quotesServiceApi.web.controllers.VoteController;
import com.kotkina.quotesServiceApi.web.models.requests.ListPage;
import com.kotkina.quotesServiceApi.web.models.requests.QuoteRequest;
import com.kotkina.quotesServiceApi.web.models.responses.QuoteResponse;
import com.kotkina.quotesServiceApi.web.models.responses.QuoteResponseList;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class QuoteMapper {

    public Quote requestToQuote(QuoteRequest request, User user) {
        if (request == null || user == null) return null;

        return Quote.builder()
                .content(request.getContent())
                .user(user)
                .build();
    }

    public Quote requestToQuote(Quote quote, QuoteRequest request) {
        if (request == null || quote == null) return null;

        quote.setContent(request.getContent());
        return quote;
    }

    public QuoteResponse quoteToResponseWithLinks(Quote quote) {
        if (quote == null) return null;

        QuoteResponse quoteResponse = quoteToResponse(quote);
        List<Link> links = new ArrayList<>();
        links.add(linkTo(methodOn(UserController.class).getUser(quote.getUser().getId()))
                .withSelfRel());
        links.add(linkTo(methodOn(VoteController.class).getVotesByQuoteId(quote.getId(), new ListPage()))
                .withSelfRel());
        quoteResponse.add(links);
        return quoteResponse;
    }

    public QuoteResponse quoteToResponse(Quote quote) {
        if (quote == null) return null;

        return QuoteResponse.builder()
                .id(quote.getId())
                .content(quote.getContent())
                .score(calculateScore(quote))
                .build();
    }

    private static long calculateScore(Quote quote) {
        if (quote == null) return 0;

        return quote.getVotes().stream()
                .mapToInt(Vote::getAssessment)
                .sum();
    }

    public QuoteResponseList quotesToResponseList(List<Quote> quotes) {
        if (quotes == null) return new QuoteResponseList();
        if (quotes.size() == 0) return new QuoteResponseList();

        return new QuoteResponseList(quotes.stream()
                .map(quote -> quoteToResponseWithLinks(quote))
                .collect(Collectors.toList()));
    }
}
