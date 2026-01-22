package ru.otus.hw.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.SecurityConfiguration;
import ru.otus.hw.dtos.GenreDto;
import ru.otus.hw.repositories.UserRepository;
import ru.otus.hw.services.GenreService;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(GenreController.class)
@Import(SecurityConfiguration.class)
class GenreControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private GenreService genreService;

    @MockitoBean
    private UserRepository userRepository;

    @Test
    @WithMockUser
    void shouldRenderGenresPageWithCorrectViewAndModelAttributes() throws Exception {
        var genres = List.of(new GenreDto(1L, "name1"), new GenreDto(2L, "name2"));
        when(genreService.findAll()).thenReturn(genres);
        mvc.perform(get("/genres"))
                .andExpect(view().name("genres"))
                .andExpect(model().attribute("genres", genres));
    }

    @Test
    void shouldRedirectToLoginPage() throws Exception {
        mvc.perform(get("/genres"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"));
    }

}