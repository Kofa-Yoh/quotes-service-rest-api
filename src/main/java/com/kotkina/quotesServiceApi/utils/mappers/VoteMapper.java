package com.kotkina.quotesServiceApi.utils.mappers;

import com.kotkina.quotesServiceApi.entities.AssessmentType;
import com.kotkina.quotesServiceApi.entities.Quote;
import com.kotkina.quotesServiceApi.entities.User;
import com.kotkina.quotesServiceApi.entities.Vote;
import com.kotkina.quotesServiceApi.web.models.requests.VoteRequest;
import com.kotkina.quotesServiceApi.web.models.responses.VoteResponseWithoutQuote;
import com.kotkina.quotesServiceApi.web.models.responses.VoteResponseList;
import com.kotkina.quotesServiceApi.web.models.responses.VoteResponseWithQuote;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class VoteMapper {

    private final QuoteMapper quoteMapper;

    private final UserMapper userMapper;

    public Vote requestToVote(VoteRequest request, Quote quote, User user) {
        return Vote.builder()
                .quote(quote)
                .user(user)
                .assessment(request.getAssessment().getScore())
                .build();
    }

    public VoteResponseWithoutQuote voteToResponseWithoutQuote(Vote vote) {
        return VoteResponseWithoutQuote.builder()
                .assessment(AssessmentType.getType(vote.getAssessment()).toString())
                .user(userMapper.userToResponse(vote.getUser()))
                .createdOn(vote.getCreatedOn())
                .build();
    }

    public VoteResponseWithQuote voteToResponseWithQuote(Vote vote) {
        return VoteResponseWithQuote.builder()
                .quote(quoteMapper.quoteToResponseWithLinks(vote.getQuote()))
                .assessment(AssessmentType.getType(vote.getAssessment()).toString())
                .user(userMapper.userToResponse(vote.getUser()))
                .createdOn(vote.getCreatedOn())
                .build();
    }

    public VoteResponseList votesToResponseWithoutQuoteList(List<Vote> votes) {
        if (votes == null) return new VoteResponseList();
        if (votes.size() == 0) return new VoteResponseList();

        return new VoteResponseList(votes.stream()
                .map(vote -> voteToResponseWithoutQuote(vote))
                .collect(Collectors.toList()));
    }

    public VoteResponseList votesToResponseWithQuoteList(List<Vote> votes) {
        if (votes == null) return new VoteResponseList();
        if (votes.size() == 0) return new VoteResponseList();

        return new VoteResponseList(votes.stream()
                .map(vote -> voteToResponseWithQuote(vote))
                .collect(Collectors.toList()));
    }
}
