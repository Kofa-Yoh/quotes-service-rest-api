package com.kotkina.quotesServiceApi.services;

import com.kotkina.quotesServiceApi.entities.Quote;
import com.kotkina.quotesServiceApi.entities.User;
import com.kotkina.quotesServiceApi.entities.Vote;
import com.kotkina.quotesServiceApi.repositories.VoteRepository;
import com.kotkina.quotesServiceApi.web.models.requests.ListPage;
import com.kotkina.quotesServiceApi.web.models.responses.EvolutionOfAssessmentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VoteService {

    private final VoteRepository voteRepository;

    public Vote save(Vote vote) {
        return voteRepository.save(vote);
    }

    public List<Vote> getVotesByQuote(Quote quote, ListPage page) {
        Pageable nextPage = PageRequest.of(page.getPage(), page.getSize());
        return voteRepository.findVotesByQuoteOrderByCreatedOnDesc(quote, nextPage)
                .getContent();
    }

    public List<Vote> getVotesByUser(User user, ListPage page) {
        Pageable nextPage = PageRequest.of(page.getPage(), page.getSize());
        return voteRepository.findVotesByUserOrderByCreatedOnDesc(user, nextPage)
                .getContent();
    }

    public List<EvolutionOfAssessmentResponse> getEvolutionOfVotesByQuote(Quote quote) {
        return voteRepository.getEvolutionOfVotesByQuoteId(quote);
    }
}
