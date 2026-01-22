package ru.otus.hw.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.SecurityConfiguration;
import ru.otus.hw.dtos.AuthorDto;
import ru.otus.hw.dtos.BookDto;
import ru.otus.hw.dtos.ErrorDto;
import ru.otus.hw.dtos.GenreDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.repositories.UserRepository;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.GenreService;

import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(BookController.class)
@Import(SecurityConfiguration.class)
class BookControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private BookService bookService;

    @MockitoBean
    private AuthorService authorService;

    @MockitoBean
    private GenreService genreService;

    @MockitoBean
    private UserRepository userRepository;

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
    @WithMockUser
    void shouldRenderBooksPageWithCorrectViewAndModelAttributesUponGetAll() throws Exception {
        when(bookService.findAll()).thenReturn(expectedBooks);
        mvc.perform(get("/"))
                .andExpect(view().name("books"))
                .andExpect(model().attribute("books", expectedBooks));
    }

    @Test
    @WithMockUser
    void shouldRenderBookEditPageWithCorrectViewAndModelAttributesUponEditBook() throws Exception {
        when(bookService.findById(1L)).thenReturn(expectedBooks.get(0));
        when(authorService.findAll()).thenReturn(expectedAuthors);
        when(genreService.findAll()).thenReturn(expectedGenres);
        mvc.perform(get("/books/edit").param("id", "1"))
                .andExpect(view().name("book-edit"))
                .andExpect(model().attribute("book", expectedBooks.get(0)))
                .andExpect(model().attribute("allAuthors", expectedAuthors))
                .andExpect(model().attribute("allGenres", expectedGenres));
    }

    @Test
    @WithMockUser
    void shouldRenderBookAddPageWithCorrectViewAndModelAttributesUponAddBook() throws Exception {
        when(authorService.findAll()).thenReturn(expectedAuthors);
        when(genreService.findAll()).thenReturn(expectedGenres);
        mvc.perform(get("/books/add").param("id", "1"))
                .andExpect(view().name("book-add"))
                .andExpect(model().attribute("book", new BookDto()))
                .andExpect(model().attribute("allAuthors", expectedAuthors))
                .andExpect(model().attribute("allGenres", expectedGenres));
    }

    @Test
    @WithMockUser
    void shouldDeleteBookAndRedirect() throws Exception {
        mvc.perform(post("/books/delete").param("id", "1"))
                .andExpect(view().name("redirect:/"));
        verify(bookService).deleteById(1L);
    }

    @Test
    @WithMockUser
    void shouldRenderErrorPageWhenPersonNotFound() throws Exception {
        when(bookService.findAll()).thenThrow(new EntityNotFoundException("message"));
        mvc.perform(get("/"))
                .andExpect(view().name("customError"))
                .andExpect(model().attribute("error", new ErrorDto(HttpStatus.NOT_FOUND, "message")));
    }

    @Test
    @WithMockUser
    void shouldRenderErrorPageWhenServerError() throws Exception {
        when(bookService.findAll()).thenThrow(new RuntimeException("message"));
        mvc.perform(get("/"))
                .andExpect(view().name("customError"))
                .andExpect(model().attribute("error", new ErrorDto(HttpStatus.INTERNAL_SERVER_ERROR, "message")));
    }

    @ParameterizedTest
    @ValueSource(strings = {"/", "/books/edit", "/books/add", "/books/delete"})
    void shouldRedirectToLoginPage(String url) throws Exception {
        mvc.perform(get(url))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"));
    }
}