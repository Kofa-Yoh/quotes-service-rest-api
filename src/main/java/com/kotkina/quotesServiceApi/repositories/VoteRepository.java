package com.kotkina.quotesServiceApi.repositories;

import com.kotkina.quotesServiceApi.entities.Quote;
import com.kotkina.quotesServiceApi.entities.User;
import com.kotkina.quotesServiceApi.entities.Vote;
import com.kotkina.quotesServiceApi.web.models.responses.EvolutionOfAssessmentResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface VoteRepository extends CrudRepository<Vote, Long> {

    Page<Vote> findVotesByQuoteOrderByCreatedOnDesc(Quote quote, Pageable nextPage);

    Page<Vote> findVotesByUserOrderByCreatedOnDesc(User user, Pageable nextPage);

    @Query(value = """
            SELECT new com.kotkina.quotesServiceApi.web.models.responses.EvolutionOfAssessmentResponse(
            v.createdOn, SUM(v.assessment) over (ORDER BY v.createdOn))
            FROM Vote v
            WHERE v.quote = :quote
            ORDER BY v.createdOn """)
    List<EvolutionOfAssessmentResponse> getEvolutionOfVotesByQuoteId(Quote quote);
}
