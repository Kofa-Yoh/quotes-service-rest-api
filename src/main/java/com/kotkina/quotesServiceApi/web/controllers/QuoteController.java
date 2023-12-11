package com.kotkina.quotesServiceApi.web.controllers;

import com.kotkina.quotesServiceApi.entities.Quote;
import com.kotkina.quotesServiceApi.entities.User;
import com.kotkina.quotesServiceApi.exceptions.EntityNotFoundException;
import com.kotkina.quotesServiceApi.services.QuoteService;
import com.kotkina.quotesServiceApi.services.UserService;
import com.kotkina.quotesServiceApi.utils.mappers.QuoteMapper;
import com.kotkina.quotesServiceApi.web.models.requests.ListPage;
import com.kotkina.quotesServiceApi.web.models.requests.QuoteRequest;
import com.kotkina.quotesServiceApi.web.models.responses.QuoteResponse;
import com.kotkina.quotesServiceApi.web.models.responses.QuoteResponseList;
import com.kotkina.quotesServiceApi.web.models.responses.SimpleResponse;
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
@RequestMapping("/api/quotes")
@RequiredArgsConstructor
@Tag(name = "Quote Manager")
public class QuoteController {

    private final QuoteService quoteService;

    private final UserService userService;

    private final QuoteMapper quoteMapper;

    @Operation(summary = "A new quote by the user")
    @PostMapping("/user/{id}/new")
    public ResponseEntity<QuoteResponse> addQuote(@Parameter(description = "User id") @PathVariable long id,
                                                  @ParameterObject @Valid QuoteRequest request) {
        User user = userService.getById(id);

        Quote quote = quoteMapper.requestToQuote(request, user);

        Quote savedQuote = quoteService.save(quote);

        return ResponseEntity.status(HttpStatus.CREATED).body(quoteMapper.quoteToResponseWithLinks(savedQuote));
    }

    @Operation(summary = "The quote with id")
    @GetMapping("/{id}")
    public ResponseEntity<QuoteResponse> getQuote(@PathVariable long id) {
        return ResponseEntity.ok(
                quoteMapper.quoteToResponseWithLinks(quoteService.getById(id))
        );
    }

    @Operation(summary = "A random quote")
    @GetMapping("/random")
    public ResponseEntity<QuoteResponse> getRandomQuote() {
        return ResponseEntity.ok(
                quoteMapper.quoteToResponseWithLinks(quoteService.getRandomQuote())
        );
    }

    @Operation(summary = "New data for the quote")
    @PutMapping("/{id}/change")
    public ResponseEntity<QuoteResponse> changeQuote(@Parameter(description = "Quote id") @PathVariable long id,
                                                     @ParameterObject @Valid QuoteRequest quoteRequest) {
        Quote oldQuote = quoteService.getById(id);
        Quote savedQuote = quoteService.save(quoteMapper.requestToQuote(oldQuote, quoteRequest));
        return ResponseEntity.ok(
                quoteMapper.quoteToResponseWithLinks(savedQuote)
        );
    }

    @Operation(summary = "Deletion of the quote")
    @DeleteMapping("/{id}/delete")
    public ResponseEntity<SimpleResponse> deleteQuote(@Parameter(description = "Quote id") @PathVariable long id) {
        if (!quoteService.existsById(id)) {
            throw new EntityNotFoundException("No quotes found");
        }

        quoteService.delete(id);
        return ResponseEntity.ok(new SimpleResponse("Quote removed, id: " + id));
    }

    @Operation(summary = "The top quotes")
    @GetMapping("/top")
    public ResponseEntity<QuoteResponseList> getTopQuotes(@Parameter(required = false) @ParameterObject ListPage page) {
        List<Quote> quotes = quoteService.getTopByVotesAssessments(page);
        if (quotes.size() == 0) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(
                quoteMapper.quotesToResponseList(quotes)
        );
    }

    @Operation(summary = "The worse quotes")
    @GetMapping("/flop")
    public ResponseEntity<QuoteResponseList> getFlopQuotes(@Parameter(required = false) @ParameterObject ListPage page) {
        List<Quote> quotes = quoteService.getFlopByVotesAssessments(page);
        if (quotes.size() == 0) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(
                quoteMapper.quotesToResponseList(quotes)
        );
    }
}
