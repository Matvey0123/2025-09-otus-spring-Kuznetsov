package ru.otus.hw.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.dtos.AuthorDto;
import ru.otus.hw.dtos.BookCreateDto;
import ru.otus.hw.dtos.BookDto;
import ru.otus.hw.dtos.BookUpdateDto;
import ru.otus.hw.dtos.ErrorDto;
import ru.otus.hw.dtos.GenreDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.services.BookService;

import java.util.List;
import java.util.Set;

import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@WebFluxTest(BookController.class)
class BookControllerTest extends MockedRepositories {

    @Autowired
    private WebTestClient client;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private BookService bookService;

    private final List<BookDto> expectedBooks = List.of(
            new BookDto("1", "title1", new AuthorDto("1", "author1"),
                    List.of(new GenreDto("1", "genre1"), new GenreDto("1", "genre2"))),
            new BookDto("2", "title2", new AuthorDto("2", "author2"),
                    List.of(new GenreDto("3", "genre3"), new GenreDto("4", "genre4"))));

    @Test
    void shouldReturnCorrectBooksList() throws Exception {
        var book1 = expectedBooks.get(0);
        var book2 = expectedBooks.get(1);
        when(bookService.findAll()).thenReturn(Flux.just(book1, book2));

        client.get().uri("/api/v1/book")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(BookDto.class)
                .hasSize(2)
                .contains(book1, book2);
    }

    @Test
    void shouldReturnCorrectBook() {
        var book = expectedBooks.get(0);
        when(bookService.findById("1")).thenReturn(Mono.just(book));

        client.get().uri("/api/v1/book/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody(BookDto.class)
                .isEqualTo(book);
    }

    @Test
    void shouldUpdateBook() throws JsonProcessingException {
        var book = new BookUpdateDto("1", "title1", "1", Set.of("1", "2"));
        var updatedBook = expectedBooks.get(1);
        when(bookService.update(book)).thenReturn(Mono.just(updatedBook));

        client.patch().uri("/api/v1/book/1")
                .contentType(APPLICATION_JSON)
                .body(Mono.just(objectMapper.writeValueAsString(book)), String.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(BookDto.class)
                .isEqualTo(updatedBook);
    }

    @Test
    void shouldAnnNewBook() throws JsonProcessingException {
        var book = new BookCreateDto("title1", "1", Set.of("1", "2"));
        var createdBook = expectedBooks.get(0);
        when(bookService.insert(book)).thenReturn(Mono.just(createdBook));

        client.post().uri("/api/v1/book")
                .contentType(APPLICATION_JSON)
                .body(Mono.just(objectMapper.writeValueAsString(book)), String.class)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.CREATED)
                .expectBody(BookDto.class)
                .isEqualTo(createdBook);
    }

    @Test
    void shouldDeleteBook() {
        when(bookService.deleteById("1")).thenReturn(Mono.empty());

        client.delete().uri("/api/v1/book/1")
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    void shouldErrorWhenPersonNotFound() throws Exception {
        when(bookService.findAll()).thenThrow(new EntityNotFoundException("message"));

        client.get().uri("/api/v1/book")
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(String.class)
                .isEqualTo(objectMapper.writeValueAsString(new ErrorDto(HttpStatus.NOT_FOUND, "message")));
    }

    @Test
    void shouldErrorWhenServerError() throws Exception {
        when(bookService.findAll()).thenThrow(new RuntimeException("message"));

        client.get().uri("/api/v1/book")
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR)
                .expectBody(String.class)
                .isEqualTo(objectMapper.writeValueAsString(new ErrorDto(HttpStatus.INTERNAL_SERVER_ERROR, "message")));
    }
}