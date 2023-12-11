package com.kotkina.quotesServiceApi.services;

import com.kotkina.quotesServiceApi.entities.Quote;
import com.kotkina.quotesServiceApi.exceptions.EntityNotFoundException;
import com.kotkina.quotesServiceApi.repositories.QuoteRepository;
import com.kotkina.quotesServiceApi.web.models.requests.ListPage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QuoteService {

    private final QuoteRepository quoteRepository;

    public Quote getById(long id) {
        return quoteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No quotes found"));
    }

    public Quote save(Quote quote) {
        return quoteRepository.save(quote);
    }

    public Quote getRandomQuote() {
        if (quoteRepository.count() == 0) {
            throw new EntityNotFoundException("No one has shared their favorite quotes yet. Be the first one");
        }
        Optional<Quote> quote = quoteRepository.getRandomQuote();
        return quote
                .orElseThrow(() -> new EntityNotFoundException("No quotes found"));

    }

    public boolean existsById(long id) {
        return quoteRepository.existsById(id);
    }

    public void delete(long id) {
        quoteRepository.deleteById(id);
    }

    public List<Quote> getTopByVotesAssessments(ListPage page) {
        Pageable nextPage = PageRequest.of(page.getPage(), page.getSize());
        return quoteRepository.getTopQuotesOrderByVotesAssessments(nextPage)
                .getContent();
    }

    public List<Quote> getFlopByVotesAssessments(ListPage page) {
        Pageable nextPage = PageRequest.of(page.getPage(), page.getSize());
        return quoteRepository.getFlopQuotesOrderByVotesAssessments(nextPage)
                .getContent();
    }
}
