package com.kotkina.quotesServiceApi.web.controllers;

import com.kotkina.quotesServiceApi.entities.Quote;
import com.kotkina.quotesServiceApi.entities.User;
import com.kotkina.quotesServiceApi.entities.Vote;
import com.kotkina.quotesServiceApi.services.QuoteService;
import com.kotkina.quotesServiceApi.services.UserService;
import com.kotkina.quotesServiceApi.services.VoteService;
import com.kotkina.quotesServiceApi.utils.mappers.VoteMapper;
import com.kotkina.quotesServiceApi.web.models.requests.ListPage;
import com.kotkina.quotesServiceApi.web.models.requests.VoteRequest;
import com.kotkina.quotesServiceApi.web.models.responses.EvolutionOfAssessmentResponse;
import com.kotkina.quotesServiceApi.web.models.responses.VoteResponseWithoutQuote;
import com.kotkina.quotesServiceApi.web.models.responses.VoteResponseList;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/votes")
@RequiredArgsConstructor
@Tag(name = "Vote Manager")
public class VoteController {

    private final VoteService voteService;

    private final QuoteService quoteService;

    private final UserService userService;

    private final VoteMapper voteMapper;

    @Operation(summary = "A new vote for the quote")
    @PostMapping("/new")
    public ResponseEntity<VoteResponseWithoutQuote> addVote(@ParameterObject @Valid VoteRequest request) {
        User user = userService.getById(request.getUserId());
        Quote quote = quoteService.getById(request.getQuoteId());

        Vote savedVote = voteService.save(voteMapper.requestToVote(request, quote, user));

        return ResponseEntity.status(HttpStatus.CREATED).body(voteMapper.voteToResponseWithoutQuote(savedVote));
    }

    @Operation(summary = "The votes for the quote")
    @GetMapping("/for-quote/{id}")
    public ResponseEntity<VoteResponseList> getVotesByQuoteId(@Parameter(description = "Quote id") @PathVariable("id") long quoteId,
                                                              @Parameter(required = false) @ParameterObject ListPage page) {
        Quote quote = quoteService.getById(quoteId);
        List<Vote> votes = voteService.getVotesByQuote(quote, page);
        if (votes.size() == 0) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(voteMapper.votesToResponseWithoutQuoteList(votes));
    }

    @Operation(summary = "The votes by the user")
    @GetMapping("/by-user/{id}")
    public ResponseEntity<VoteResponseList> getVotesByUserId(@Parameter(description = "User id") @PathVariable("id") long userId,
                                                             @Parameter(required = false) @ParameterObject ListPage page) {
        User user = userService.getById(userId);
        List<Vote> votes = voteService.getVotesByUser(user, page);
        if (votes.size() == 0) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(voteMapper.votesToResponseWithQuoteList(votes));
    }

    @Operation(summary = "The evolution of the quote's votes")
    @GetMapping("/evolution/quote/{id}")
    public ResponseEntity<List<EvolutionOfAssessmentResponse>> getEvolutionOfVotesByQuoteId(@Parameter(description = "Quote id") @PathVariable long id) {
        Quote quote = quoteService.getById(id);
        List<EvolutionOfAssessmentResponse> evolutionOfVotesByQuote = voteService.getEvolutionOfVotesByQuote(quote);
        if (evolutionOfVotesByQuote.size() == 0) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(evolutionOfVotesByQuote);
    }
}
