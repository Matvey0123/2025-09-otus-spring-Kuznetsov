package ru.otus.hw.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import ru.otus.hw.dtos.CommentDto;
import ru.otus.hw.services.CommentService;

import static org.mockito.Mockito.when;

@WebFluxTest(CommentController.class)
class CommentControllerTest extends MockedRepositories {

    @Autowired
    private WebTestClient client;

    @MockitoBean
    private CommentService commentService;

    @Test
    void shouldReturnCorrectCommentsListByBookId() {
        var comment1 = new CommentDto("1", "name1");
        var comment2 = new CommentDto("2", "name2");
        when(commentService.findByBookId("1")).thenReturn(Flux.just(comment1, comment2));

        client.get().uri("/api/v1/book/1/comment")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(CommentDto.class)
                .hasSize(2)
                .contains(comment1, comment2);
    }

}