package ru.otus.hw.services;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(CommentServiceImpl.class)
@Transactional(propagation = Propagation.NEVER)
class CommentServiceImplTest {

    @Autowired
    private CommentService service;

    @Test
    @Order(1)
    void shouldFindById() {
        var actualComment = service.findById(1L).get();
        assertThat(actualComment.getId()).isEqualTo(1L);
        assertThat(actualComment.getText()).isEqualTo("comment_1");
        assertThat(actualComment.getBook().getId()).isEqualTo(1L);
    }

    @Test
    @Order(2)
    void shouldFindByBookId() {
        var comments = service.findByBookId(1L);
        assertThat(comments).isNotEmpty()
                .allMatch(c -> c.getText() != null && c.getBook().getId() == 1L);
    }

    @Test
    @Order(3)
    void shouldInsertComment() {
        var newComment = service.insert("newText", 2L);
        assertThat(newComment.getId()).isEqualTo(10L);
        assertThat(newComment.getText()).isEqualTo("newText");
        assertThat(newComment.getBook().getId()).isEqualTo(2L);
    }

    @Test
    @Order(4)
    void shouldUpdateComment() {
        var updatedComment = service.update(3L, "newText2");
        assertThat(updatedComment.getId()).isEqualTo(3L);
        assertThat(updatedComment.getText()).isEqualTo("newText2");
        assertThat(updatedComment.getBook().getId()).isEqualTo(1L);
    }
}