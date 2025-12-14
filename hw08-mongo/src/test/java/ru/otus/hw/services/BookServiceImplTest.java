package ru.otus.hw.services;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.MongoOperations;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Genre;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
@Import(BookServiceImpl.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class BookServiceImplTest {

    @Autowired
    private BookService service;

    @Autowired
    private MongoOperations operations;

    @Test
    @Order(1)
    void shouldFindOneBook() {
        var actualBook = service.findById("1").get();
        var expectedBook = new Book("1", "BookTitle_1", new Author("1", "Author_1"),
                List.of(new Genre("1", "Genre_1"), new Genre("2", "Genre_2")));
        assertThat(actualBook).usingRecursiveComparison().isEqualTo(expectedBook);
    }

    @Test
    @Order(2)
    void shouldFindAllBooks() {
        var actualBooks = service.findAll();
        assertThat(actualBooks).hasSize(3)
                .allMatch(b -> b.getTitle() != null &&
                        b.getAuthor().getFullName() != null &&
                        b.getGenres().stream().allMatch(g -> g.getName() != null));
    }

    @Test
    @Order(3)
    void shouldInsertBook() {
        var newBook = service.insert("newTitle", "1", Set.of("1", "2"));
        var expectedBook = new Book(null, "newTitle", new Author("1", "Author_1"),
                List.of(new Genre("1", "Genre_1"), new Genre("2", "Genre_2")));
        assertThat(newBook).usingRecursiveComparison().ignoringFields("id").isEqualTo(expectedBook);

        assertThat(operations.findById(newBook.getId(), Book.class))
                .usingRecursiveComparison().ignoringFields("id").isEqualTo(expectedBook);
    }

    @Test
    @Order(4)
    void shouldUpdateBook() {
        var updatedBook = service.update("3", "newTitle", "2", Set.of("3", "4"));
        var expectedBook = new Book("3", "newTitle", new Author("2", "Author_2"),
                List.of(new Genre("3", "Genre_3"), new Genre("4", "Genre_4")));
        assertThat(updatedBook).usingRecursiveComparison().isEqualTo(expectedBook);
    }

    @Test
    @Order(5)
    void shouldDeleteBookWithComments() {
        var comment1 = operations.findById("1", Comment.class);
        var comment2 = operations.findById("2", Comment.class);
        var comment3 = operations.findById("3", Comment.class);
        assertThat(comment1.getBookId()).isEqualTo("1");
        assertThat(comment2.getBookId()).isEqualTo("1");
        assertThat(comment3.getBookId()).isEqualTo("1");

        service.deleteById("1");

        assertThat(operations.findById("1", Book.class)).isNull();
        assertThat(operations.findById("1", Comment.class)).isNull();
        assertThat(operations.findById("2", Comment.class)).isNull();
        assertThat(operations.findById("3", Comment.class)).isNull();
    }
}