package ru.otus.hw.services;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.MongoOperations;
import ru.otus.hw.models.Comment;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
@Import(CommentServiceImpl.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CommentServiceImplTest {

    @Autowired
    private CommentService service;

    @Autowired
    private MongoOperations operations;

    @Test
    @Order(1)
    void shouldFindById() {
        var actualComment = service.findById("1").get();
        assertThat(actualComment.getId()).isEqualTo("1");
        assertThat(actualComment.getText()).isEqualTo("comment_1");
        assertThat(actualComment.getBookId()).isEqualTo("1");
    }

    @Test
    @Order(2)
    void shouldFindByBookId() {
        var comments = service.findByBookId("1");
        assertThat(comments).isNotEmpty()
                .allMatch(c -> c.getText() != null && "1".equals(c.getBookId()));
    }

    @Test
    @Order(3)
    void shouldInsertComment() {
        var newComment = service.insert("newText", "2");
        assertThat(newComment.getText()).isEqualTo("newText");
        assertThat(newComment.getBookId()).isEqualTo("2");

        var allComments = operations.findAll(Comment.class);
        assertThat(allComments).hasSize(10)
                .anyMatch(c -> "newText".equals(c.getText()) && "2".equals(c.getBookId()));
    }

    @Test
    @Order(4)
    void shouldUpdateComment() {
        var updatedComment = service.update("3", "newText2");
        assertThat(updatedComment.getId()).isEqualTo("3");
        assertThat(updatedComment.getText()).isEqualTo("newText2");
        assertThat(updatedComment.getBookId()).isEqualTo("1");
    }

    @Test
    @Order(5)
    void shouldDeleteComment() {
        service.deleteById("1");
        var deletedComment = operations.findById("1", Comment.class);
        assertThat(deletedComment).isNull();
    }
}