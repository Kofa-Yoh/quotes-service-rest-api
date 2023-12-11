package com.kotkina.quotesServiceApi.web.controllers;

import com.kotkina.quotesServiceApi.entities.Quote;
import com.kotkina.quotesServiceApi.entities.User;
import com.kotkina.quotesServiceApi.services.QuoteService;
import com.kotkina.quotesServiceApi.services.UserService;
import com.kotkina.quotesServiceApi.utils.mappers.QuoteMapper;
import com.kotkina.quotesServiceApi.web.models.requests.ListPage;
import com.kotkina.quotesServiceApi.web.models.requests.QuoteRequest;
import com.kotkina.quotesServiceApi.web.models.responses.QuoteResponse;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.Link;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.refEq;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
class QuoteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private QuoteService quoteService;

    @MockBean
    private UserService userService;

    @MockBean
    private QuoteMapper quoteMapper;

    @Test
    void addQuote_Status201() throws Exception {
        long userId = 2L;
        User user = new User(userId, "", "", "", null, new ArrayList<>(), new ArrayList<>());
        long quoteId = 1L;
        String content = "Testing does not detect errors such as creating the wrong application.";
        QuoteRequest request = new QuoteRequest("Testing does not detect errors such as creating the wrong application.");
        Quote newQuote = new Quote(quoteId, content, null, null, user, new ArrayList<>());
        Quote savedQuote = new Quote(quoteId, content, null, null, user, new ArrayList<>());
        QuoteResponse quoteResponse = new QuoteResponse(savedQuote.getId(), savedQuote.getContent(), 0);
        List<Link> links = new ArrayList<>();
        links.add(linkTo(methodOn(UserController.class).getUser(savedQuote.getUser().getId()))
                .withSelfRel());
        links.add(linkTo(methodOn(VoteController.class).getVotesByQuoteId(savedQuote.getId(), new ListPage()))
                .withSelfRel());
        quoteResponse.add(links);

        Mockito.when(userService.getById(userId)).thenReturn(user);
        Mockito.when(quoteMapper.requestToQuote(refEq(request), eq(user))).thenReturn(newQuote);
        Mockito.when(quoteService.save(newQuote)).thenReturn(savedQuote);
        Mockito.when(quoteMapper.quoteToResponseWithLinks(savedQuote)).thenReturn(quoteResponse);

        mockMvc.perform(post("/api/quotes/user/" + userId + "/new")
                        .param("content", content))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.content", is(content)));

        Mockito.verify(userService, Mockito.times(1)).getById(userId);
        Mockito.verify(quoteMapper, Mockito.times(1)).requestToQuote(refEq(request), eq(user));
        Mockito.verify(quoteService, Mockito.times(1)).save(newQuote);
    }

    @Test
    void getQuote_Status200() throws Exception {
        User user = new User(2L, "", "", "", null, new ArrayList<>(), new ArrayList<>());

        String content = "Testing does not detect errors such as creating the wrong application.";
        Quote quote = new Quote(1L, content, null, null, user, new ArrayList<>());
        QuoteResponse quoteResponse = new QuoteResponse(quote.getId(), quote.getContent(), 0);
        List<Link> links = new ArrayList<>();
        links.add(linkTo(methodOn(UserController.class).getUser(quote.getUser().getId()))
                .withSelfRel());
        links.add(linkTo(methodOn(VoteController.class).getVotesByQuoteId(quote.getId(), new ListPage()))
                .withSelfRel());
        quoteResponse.add(links);

        Mockito.when(quoteService.getById(1L)).thenReturn(quote);
        Mockito.when(quoteMapper.quoteToResponseWithLinks(quote)).thenReturn(quoteResponse);

        mockMvc.perform(get("/api/quotes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", is(content)))
                .andExpect(jsonPath("$._links.*.*.href", hasItem("http://localhost/api/users/2")))
                .andExpect(jsonPath("$._links.*.*.href", hasItem("http://localhost/api/votes/for-quote/1")));

        Mockito.verify(quoteService, Mockito.times(1)).getById(1L);
    }

    @Test
    void changeQuote_Status200() throws Exception {
        long userId = 2L;
        User user = new User(userId, "", "", "", null, new ArrayList<>(), new ArrayList<>());
        long quoteId = 1L;
        String content1 = "Old content";
        String content2 = "New content";
        QuoteRequest quoteRequest = new QuoteRequest(content2);
        Quote oldQuote = new Quote(quoteId, content1, null, null, user, new ArrayList<>());
        Quote newQuote = new Quote(quoteId, content2, null, null, user, new ArrayList<>());
        Quote savedQuote = new Quote(quoteId, content2, null, null, user, new ArrayList<>());
        QuoteResponse quoteResponse = new QuoteResponse(savedQuote.getId(), savedQuote.getContent(), 0);
        List<Link> links = new ArrayList<>();
        links.add(linkTo(methodOn(UserController.class).getUser(savedQuote.getUser().getId()))
                .withSelfRel());
        links.add(linkTo(methodOn(VoteController.class).getVotesByQuoteId(savedQuote.getId(), new ListPage()))
                .withSelfRel());
        quoteResponse.add(links);

        Mockito.when(quoteService.getById(quoteId)).thenReturn(oldQuote);
        Mockito.when(quoteMapper.requestToQuote(eq(oldQuote), refEq(quoteRequest))).thenReturn(newQuote);
        Mockito.when(quoteService.save(newQuote)).thenReturn(savedQuote);
        Mockito.when(quoteMapper.quoteToResponseWithLinks(savedQuote)).thenReturn(quoteResponse);

        mockMvc.perform(put("/api/quotes/" + quoteId + "/change")
                        .param("content", content2))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", is(content2)));

        Mockito.verify(quoteService, Mockito.times(1)).getById(quoteId);
        Mockito.verify(quoteMapper, Mockito.times(1)).requestToQuote(eq(oldQuote), refEq(quoteRequest));
        Mockito.verify(quoteService, Mockito.times(1)).save(newQuote);
        Mockito.verify(quoteMapper, Mockito.times(1)).quoteToResponseWithLinks(savedQuote);
    }

    @Test
    void deleteQuote_Status200() throws Exception {
        long quoteId = 1;
        Mockito.when(quoteService.existsById(quoteId)).thenReturn(true);

        mockMvc.perform(delete("/api/quotes/" + quoteId + "/delete"))
                .andExpect(status().isOk());
    }
}