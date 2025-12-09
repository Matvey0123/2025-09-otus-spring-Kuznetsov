package ru.otus.hw.services;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Genre;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(BookServiceImpl.class)
@Transactional(propagation = Propagation.NEVER)
class BookServiceImplTest {

    @Autowired
    private BookService service;

    @Autowired
    private TestEntityManager em;

    @Test
    @Order(1)
    void shouldFindOneBook() {
        var actualBook = service.findById(1L).get();
        var expectedBook = new Book(1L, "BookTitle_1", new Author(1L, "Author_1"),
                List.of(new Genre(1L, "Genre_1"), new Genre(2L, "Genre_2")));
        assertThat(actualBook).usingRecursiveComparison().isEqualTo(expectedBook);
    }

    @Test
    @Order(2)
    void shouldFindAllBooks() {
        var actualBooks = service.findAll();
        assertThat(actualBooks).isNotEmpty()
                .allMatch(b -> b.getTitle() != null &&
                        b.getAuthor().getFullName() != null &&
                        b.getGenres().stream().allMatch(g -> g.getName() != null));
    }

    @Test
    @Order(3)
    void shouldInsertBook() {
        var newBook = service.insert("newTitle", 1L, Set.of(1L, 2L));
        var expectedBook = new Book(4L, "newTitle", new Author(1L, "Author_1"),
                List.of(new Genre(1L, "Genre_1"), new Genre(2L, "Genre_2")));
        assertThat(newBook).usingRecursiveComparison().isEqualTo(expectedBook);
    }

    @Test
    @Order(4)
    void shouldUpdateBook() {
        var updatedBook = service.update(3L, "newTitle", 2L, Set.of(3L, 4L));
        var expectedBook = new Book(3L, "newTitle", new Author(2L, "Author_2"),
                List.of(new Genre(3L, "Genre_3"), new Genre(4L, "Genre_4")));
        assertThat(updatedBook).usingRecursiveComparison().isEqualTo(expectedBook);
    }

    @Test
    @Order(5)
    @Transactional
    void shouldDeleteBookWithComments() {
        var book = em.find(Book.class, 1L);
        assertThat(book).isNotNull();
        var comment1 = em.find(Comment.class, 1L);
        var comment2 = em.find(Comment.class, 2L);
        var comment3 = em.find(Comment.class, 3L);
        assertThat(comment1.getBook().getId()).isEqualTo(1L);
        assertThat(comment2.getBook().getId()).isEqualTo(1L);
        assertThat(comment3.getBook().getId()).isEqualTo(1L);

        service.deleteById(1L);

        assertThat(em.find(Book.class, 1L)).isNull();
        assertThat(em.find(Comment.class, 1L)).isNull();
        assertThat(em.find(Comment.class, 2L)).isNull();
        assertThat(em.find(Comment.class, 3L)).isNull();
    }
}