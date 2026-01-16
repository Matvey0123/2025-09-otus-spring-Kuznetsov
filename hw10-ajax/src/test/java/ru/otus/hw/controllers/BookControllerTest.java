package ru.otus.hw.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
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

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookController.class)
class BookControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockitoBean
    private BookService bookService;

    private final List<BookDto> expectedBooks = List.of(
            new BookDto(1L, "title1", new AuthorDto(1L, "author1"),
                    List.of(new GenreDto(1L, "genre1"), new GenreDto(2L, "genre2"))),
            new BookDto(2L, "title2", new AuthorDto(2L, "author2"),
                    List.of(new GenreDto(3L, "genre3"), new GenreDto(4L, "genre4"))));

    private final List<AuthorDto> expectedAuthors = List.of(new AuthorDto(1L, "author1"),
            new AuthorDto(2L, "author2"));

    private final List<GenreDto> expectedGenres = List.of(new GenreDto(1L, "genre1"),
            new GenreDto(2L, "genre2"), new GenreDto(3L, "genre3"),
            new GenreDto(4L, "genre4"));

    @Test
    void shouldReturnCorrectBooksList() throws Exception {
        when(bookService.findAll()).thenReturn(expectedBooks);
        mvc.perform(get("/api/v1/book"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(expectedBooks)));
    }

    @Test
    void shouldReturnCorrectBook() throws Exception {
        when(bookService.findById(1L)).thenReturn(expectedBooks.get(0));
        mvc.perform(get("/api/v1/book/1"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(expectedBooks.get(0))));
    }

    @Test
    void shouldUpdateBook() throws Exception {
        var book = new BookUpdateDto(1L, "title1", 1L, Set.of(1L, 2L));
        var updatedBook = expectedBooks.get(1);
        when(bookService.update(book)).thenReturn(updatedBook);
        mvc.perform(patch("/api/v1/book/1").contentType(APPLICATION_JSON)
                        .content(mapper.writeValueAsString(book)))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(updatedBook)));
    }

    @Test
    void shouldAnnNewBook() throws Exception {
        var book = new BookCreateDto("title1", 1L, Set.of(1L, 2L));
        var createdBook = expectedBooks.get(0);
        when(bookService.insert(book)).thenReturn(createdBook);
        mvc.perform(post("/api/v1/book").contentType(APPLICATION_JSON)
                        .content(mapper.writeValueAsString(book)))
                .andExpect(status().is(201))
                .andExpect(content().json(mapper.writeValueAsString(createdBook)));
    }

    @Test
    void shouldDeleteBook() throws Exception {
        mvc.perform(delete("/api/v1/book/1"))
                .andExpect(status().is(204));
        verify(bookService).deleteById(1L);
    }

    @Test
    void shouldErrorWhenPersonNotFound() throws Exception {
        when(bookService.findAll()).thenThrow(new EntityNotFoundException("message"));
        mvc.perform(get("/api/v1/book"))
                .andExpect(status().isNotFound())
                .andExpect(content().json(mapper.writeValueAsString(new ErrorDto(HttpStatus.NOT_FOUND, "message"))));
    }

    @Test
    void shouldErrorWhenServerError() throws Exception {
        when(bookService.findAll()).thenThrow(new RuntimeException("message"));
        mvc.perform(get("/api/v1/book"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().json(mapper.writeValueAsString(new ErrorDto(HttpStatus.INTERNAL_SERVER_ERROR, "message"))));
    }
}