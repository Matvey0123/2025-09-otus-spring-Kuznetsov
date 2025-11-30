package ru.otus.hw.repositories;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(CommentRepositoryImpl.class)
class CommentRepositoryImplTest {

    private static final long FIRST_COMMENT_ID = 1L;
    private static final int EXPECTED_COMMENTS_COUNT = 3;

    @Autowired
    private CommentRepository repository;

    @Autowired
    private TestEntityManager em;

    @Test
    void shouldReturnCorrectCommentById() {
        var actualComment = repository.findById(FIRST_COMMENT_ID);
        var expectedComment = em.find(Comment.class, FIRST_COMMENT_ID);
        assertThat(actualComment).isPresent()
                .get()
                .isEqualTo(expectedComment);
    }

    @Test
    void shouldReturnCorrectCommentsByBookId() {
        var actualComments = repository.findByBookId(1L);
        assertThat(actualComments).isNotNull().hasSize(EXPECTED_COMMENTS_COUNT)
                .allMatch(b -> !b.getText().equals(""))
                .allMatch(b -> b.getBook().getTitle() != null);
    }

    @Test
    void shouldSaveNewComment() {
        var book = em.find(Book.class, 1L);
        var expectedComment = new Comment(0, "Comment_100500", book);
        var returnedComment = repository.save(expectedComment);
        assertThat(returnedComment).isNotNull()
                .matches(comment -> comment.getId() > 0)
                .usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(expectedComment);

        assertThat(em.find(Comment.class, returnedComment.getId())).isEqualTo(returnedComment);
    }

    @Test
    void shouldSaveUpdatedComment() {
        var savedComment = em.find(Comment.class, 1L);
        savedComment.setText("newText");

        var returnedComment = repository.save(savedComment);
        assertThat(returnedComment).isNotNull()
                .matches(b -> b.getId() > 0)
                .matches(b -> b.getBook().getTitle() != null)
                .usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(savedComment);

        assertThat(em.find(Comment.class, returnedComment.getId())).isEqualTo(returnedComment);
    }

    @Test
    void shouldDeleteComment() {
        assertThat(em.find(Comment.class, 1L)).isNotNull();
        repository.deleteById(1L);
        assertThat(em.find(Comment.class, 1L)).isNull();
    }

}