package ru.otus.hw.repositories;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Genre;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({BookRepositoryImpl.class, CommentRepositoryImpl.class})
class BookRepositoryImplTest {

    private static final long FIRST_BOOK_ID = 1L;
    private static final int EXPECTED_BOOKS_COUNT = 3;

    @Autowired
    private BookRepository repository;

    @Autowired
    private TestEntityManager em;

    @Test
    void shouldReturnCorrectBookById() {
        var actualBook = repository.findById(FIRST_BOOK_ID);
        var expectedBook = em.find(Book.class, FIRST_BOOK_ID);
        assertThat(actualBook).isPresent()
                .get()
                .isEqualTo(expectedBook);
    }

    @Test
    void shouldReturnCorrectBooksList() {
        var actualBooks = repository.findAll();
        assertThat(actualBooks).isNotNull().hasSize(EXPECTED_BOOKS_COUNT)
                .allMatch(b -> !b.getTitle().equals(""))
                .allMatch(b -> b.getAuthor().getFullName() != null)
                .allMatch(b -> b.getGenres() != null && b.getGenres().size() == 2);
    }

    @Test
    void shouldSaveNewBook() {
        var author2 = em.find(Author.class, 2L);
        var genre3 = em.find(Genre.class, 3L);
        var genre4 = em.find(Genre.class, 4L);
        List<Genre> newGenres = new ArrayList<>();
        newGenres.add(genre3);
        newGenres.add(genre4);
        var expectedBook = new Book(0, "BookTitle_10500", author2, newGenres);
        var returnedBook = repository.save(expectedBook);
        assertThat(returnedBook).isNotNull()
                .matches(book -> book.getId() > 0)
                .usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(expectedBook);

        assertThat(em.find(Book.class, returnedBook.getId())).isEqualTo(returnedBook);
    }

    @Test
    void shouldSaveUpdatedBook() {
        var savedBook = em.find(Book.class, 1L);
        var author2 = em.find(Author.class, 2L);
        var genre3 = em.find(Genre.class, 3L);
        var genre4 = em.find(Genre.class, 4L);
        List<Genre> newGenres = new ArrayList<>();
        newGenres.add(genre3);
        newGenres.add(genre4);

        savedBook.setTitle("newTitle");
        savedBook.setAuthor(author2);
        savedBook.setGenres(newGenres);

        var returnedBook = repository.save(savedBook);
        assertThat(returnedBook).isNotNull()
                .matches(b -> b.getId() > 0)
                .matches(b -> b.getAuthor().getFullName() != null)
                .matches(b -> b.getGenres().size() > 0)
                .usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(savedBook);

        assertThat(em.find(Book.class, returnedBook.getId())).isEqualTo(returnedBook);
    }

    @Test
    void shouldDeleteBook() {
        assertThat(em.find(Book.class, 1L)).isNotNull();
        repository.deleteById(1L);
        assertThat(em.find(Book.class, 1L)).isNull();

        assertThat(em.find(Comment.class, 1L)).isNull();
        assertThat(em.find(Comment.class, 2L)).isNull();
        assertThat(em.find(Comment.class, 3L)).isNull();
    }
}