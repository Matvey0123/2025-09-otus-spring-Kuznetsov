package ru.otus.hw.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.SecurityConfiguration;
import ru.otus.hw.dtos.AuthorDto;
import ru.otus.hw.repositories.UserRepository;
import ru.otus.hw.services.AuthorService;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(AuthorController.class)
@Import(SecurityConfiguration.class)
class AuthorControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private AuthorService authorService;

    @MockitoBean
    private UserRepository userRepository;

    @Test
    @WithMockUser(roles = "VIEWER")
    void shouldRenderAuthorsPageWithCorrectViewAndModelAttributes() throws Exception {
        var authors = List.of(new AuthorDto(1L, "name1"), new AuthorDto(2L, "name2"));
        when(authorService.findAll()).thenReturn(authors);
        mvc.perform(get("/authors"))
                .andExpect(view().name("authors"))
                .andExpect(model().attribute("authors", authors));
    }

    @Test
    void shouldRedirectToLoginPage() throws Exception {
        mvc.perform(get("/authors"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"));
    }

}