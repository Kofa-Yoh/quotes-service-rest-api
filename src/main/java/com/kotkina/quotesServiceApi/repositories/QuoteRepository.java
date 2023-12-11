package com.kotkina.quotesServiceApi.repositories;

import com.kotkina.quotesServiceApi.entities.Quote;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface QuoteRepository extends CrudRepository<Quote, Long> {

    @Query(nativeQuery = true,
            value = "SELECT q1.* FROM quote AS q1 JOIN (SELECT id FROM quote ORDER BY RAND() LIMIT 1) AS q2 ON q1.id=q2.id")
    Optional<Quote> getRandomQuote();

    String queryForTopQuotesByVotesAssessments =
            "SELECT * FROM quote q " +
                    "JOIN (SELECT sum(v.assessment) AS sum, quote_id FROM vote v " +
                    "       GROUP BY v.quote_id" +
                    "     ) t ON t.quote_id = q.id " +
                    "WHERE t.sum > 0 " +
                    "ORDER BY t.sum DESC";

    String queryForFlopQuotesByVotesAssessments =
            "SELECT * FROM quote q " +
                    "JOIN (SELECT sum(v.assessment) AS sum, quote_id FROM vote v " +
                    "       GROUP BY v.quote_id" +
                    "     ) t ON t.quote_id = q.id " +
                    "WHERE t.sum < 0 " +
                    "ORDER BY t.sum ASC";

    @Query(value = queryForTopQuotesByVotesAssessments,
            nativeQuery = true)
    Page<Quote> getTopQuotesOrderByVotesAssessments(Pageable nextPage);

    @Query(value = queryForFlopQuotesByVotesAssessments,
            nativeQuery = true)
    Page<Quote> getFlopQuotesOrderByVotesAssessments(Pageable nextPage);
}
