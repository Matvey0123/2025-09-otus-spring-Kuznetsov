package ru.otus.hw.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import ru.otus.hw.dtos.AuthorDto;
import ru.otus.hw.services.AuthorService;

import static org.mockito.Mockito.when;

@WebFluxTest(AuthorController.class)
class AuthorControllerTest extends MockedRepositories {

    @Autowired
    private WebTestClient client;

    @MockitoBean
    private AuthorService authorService;

    @Test
    void shouldReturnCorrectAuthorsList() {
        var author1 = new AuthorDto("1", "name1");
        var author2 = new AuthorDto("2", "name2");
        when(authorService.findAll()).thenReturn(Flux.just(author1, author2));

        client.get().uri("/api/v1/author")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(AuthorDto.class)
                .hasSize(2)
                .contains(author1, author2);
    }

}