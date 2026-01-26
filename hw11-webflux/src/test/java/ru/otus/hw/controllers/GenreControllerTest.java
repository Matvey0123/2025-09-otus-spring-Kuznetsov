package ru.otus.hw.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import ru.otus.hw.dtos.GenreDto;
import ru.otus.hw.services.GenreService;

import static org.mockito.Mockito.when;

@WebFluxTest(GenreController.class)
class GenreControllerTest extends MockedRepositories {

    @Autowired
    private WebTestClient client;

    @MockitoBean
    private GenreService genreService;

    @Test
    void shouldReturnCorrectGenresList() {
        var genre1 = new GenreDto("1", "name1");
        var genre2 = new GenreDto("2", "name2");
        when(genreService.findAll()).thenReturn(Flux.just(genre1, genre2));

        client.get().uri("/api/v1/genre")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(GenreDto.class)
                .hasSize(2)
                .contains(genre1, genre2);
    }

}