package ru.otus.hw.services;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import ru.otus.hw.repositories.BookRepositoryImpl;
import ru.otus.hw.repositories.CommentRepositoryImpl;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({CommentServiceImpl.class, CommentRepositoryImpl.class, BookRepositoryImpl.class})
class CommentServiceImplTest {

    @Autowired
    private CommentService service;

    @Test
    void shouldFindById() {
        var actualComment = service.findById(1L).get();
        assertThat(actualComment.getId()).isEqualTo(1L);
        assertThat(actualComment.getText()).isEqualTo("comment_1");
        assertThat(actualComment.getBook().getId()).isEqualTo(1L);
    }

    @Test
    void shouldFindByBookId() {
        var comments = service.findByBookId(1L);
        assertThat(comments).isNotEmpty()
                .allMatch(c -> c.getText() != null && c.getBook().getId() == 1L);
    }

    @Test
    void shouldInsertComment() {
        var newComment = service.insert("newText", 2L);
        assertThat(newComment.getId()).isEqualTo(10L);
        assertThat(newComment.getText()).isEqualTo("newText");
        assertThat(newComment.getBook().getId()).isEqualTo(2L);
    }

    @Test
    void shouldUpdateComment() {
        var updatedComment = service.update(3L, "newText2");
        assertThat(updatedComment.getId()).isEqualTo(3L);
        assertThat(updatedComment.getText()).isEqualTo("newText2");
        assertThat(updatedComment.getBook().getId()).isEqualTo(1L);
    }
}