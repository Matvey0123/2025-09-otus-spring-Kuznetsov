package ru.otus.hw.services;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.converters.AuthorConverter;
import ru.otus.hw.converters.BookConverter;
import ru.otus.hw.converters.CommentConverter;
import ru.otus.hw.converters.GenreConverter;
import ru.otus.hw.repositories.AuthorRepositoryImpl;
import ru.otus.hw.repositories.BookRepositoryImpl;
import ru.otus.hw.repositories.CommentRepositoryImpl;
import ru.otus.hw.repositories.GenreRepositoryImpl;

import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

@DataJpaTest
@Import({BookServiceImpl.class, AuthorRepositoryImpl.class, GenreRepositoryImpl.class, CommentRepositoryImpl.class,
        BookRepositoryImpl.class, BookConverter.class, AuthorConverter.class, GenreConverter.class,
        CommentConverter.class})
@Transactional(propagation = Propagation.NEVER)
class BookServiceImplTest {

    @Autowired
    private BookService service;

    @Autowired
    private BookConverter converter;

    @Test
    void shouldNoLazyInitializationExceptionUponFindOneBook() {
        var throwable = catchThrowable(() -> service.findById(1L).map(converter::bookToString));
        assertThat(throwable).isNull();
    }

    @Test
    void shouldNoLazyInitializationExceptionUponFindAllBooks() {
        var throwable = catchThrowable(() -> service.findAll().stream()
                .map(converter::bookToString).collect(Collectors.joining()));
        assertThat(throwable).isNull();
    }

    @Test
    void shouldNoLazyInitializationExceptionUponInsertBook() {
        var throwable = catchThrowable(() ->
                converter.bookToString(service.insert("newTitle", 1L, Set.of(1L, 2L))));
        assertThat(throwable).isNull();
    }

    @Test
    void shouldNoLazyInitializationExceptionUponUpdateBook() {
        var throwable = catchThrowable(() ->
                converter.bookToString(service.update(1L, "newTitle", 2L, Set.of(3L, 4L))));
        assertThat(throwable).isNull();
    }
}