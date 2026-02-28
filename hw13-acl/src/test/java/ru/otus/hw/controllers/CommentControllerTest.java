package ru.otus.hw.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.SecurityConfiguration;
import ru.otus.hw.dtos.CommentDto;
import ru.otus.hw.repositories.UserRepository;
import ru.otus.hw.services.CommentService;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(CommentController.class)
@Import(SecurityConfiguration.class)
class CommentControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private CommentService commentService;

    @MockitoBean
    private UserRepository userRepository;

    @Test
    @WithMockUser(roles = "VIEWER")
    void shouldRenderCommentsPageWithCorrectViewAndModelAttributes() throws Exception {
        var comments = List.of(new CommentDto(1L, "name1"), new CommentDto(2L, "name2"));
        when(commentService.findByBookId(1L)).thenReturn(comments);
        mvc.perform(get("/comments").param("bookId", "1"))
                .andExpect(view().name("comments"))
                .andExpect(model().attribute("comments", comments));
    }

    @Test
    void shouldRedirectToLoginPage() throws Exception {
        mvc.perform(get("/comments"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"));
    }

}