package ru.otus.hw.services;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.AuthorRepositoryImpl;
import ru.otus.hw.repositories.BookRepositoryImpl;
import ru.otus.hw.repositories.CommentRepositoryImpl;
import ru.otus.hw.repositories.GenreRepositoryImpl;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({BookServiceImpl.class, AuthorRepositoryImpl.class, GenreRepositoryImpl.class, CommentRepositoryImpl.class,
        BookRepositoryImpl.class})
@Transactional(propagation = Propagation.NEVER)
class BookServiceImplTest {

    @Autowired
    private BookService service;

    @Test
    void shouldFindOneBook() {
        var actualBook = service.findById(1L).get();
        var expectedBook = new Book(1L, "BookTitle_1", new Author(1L, "Author_1"),
                List.of(new Genre(1L, "Genre_1"), new Genre(2L, "Genre_2")));
        assertThat(actualBook).usingRecursiveComparison().isEqualTo(expectedBook);
    }

    @Test
    void shouldFindAllBooks() {
        var actualBooks = service.findAll();
        assertThat(actualBooks).isNotEmpty()
                .allMatch(b -> b.getTitle() != null &&
                        b.getAuthor().getFullName() != null &&
                        b.getGenres().stream().allMatch(g -> g.getName() != null));
    }

    @Test
    void shouldInsertBook() {
        var newBook = service.insert("newTitle", 1L, Set.of(1L, 2L));
        var expectedBook = new Book(4L, "newTitle", new Author(1L, "Author_1"),
                List.of(new Genre(1L, "Genre_1"), new Genre(2L, "Genre_2")));
        assertThat(newBook).usingRecursiveComparison().isEqualTo(expectedBook);
    }

    @Test
    void shouldUpdateBook() {
        var updatedBook = service.update(3L, "newTitle", 2L, Set.of(3L, 4L));
        var expectedBook = new Book(3L, "newTitle", new Author(2L, "Author_2"),
                List.of(new Genre(3L, "Genre_3"), new Genre(4L, "Genre_4")));
        assertThat(updatedBook).usingRecursiveComparison().isEqualTo(expectedBook);
    }
}