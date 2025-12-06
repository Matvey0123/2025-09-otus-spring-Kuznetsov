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
@Import(BookRepositoryImpl.class)
class BookRepositoryImplTest {

    @Autowired
    private BookRepository repository;

    @Autowired
    private TestEntityManager em;

    @Test
    void shouldDeleteBookWithComments() {
        assertThat(em.find(Book.class, 1L)).isNotNull();
        repository.deleteByIdWithComments(1L);
        assertThat(em.find(Book.class, 1L)).isNull();

        assertThat(em.find(Comment.class, 1L)).isNull();
        assertThat(em.find(Comment.class, 2L)).isNull();
        assertThat(em.find(Comment.class, 3L)).isNull();
    }
}