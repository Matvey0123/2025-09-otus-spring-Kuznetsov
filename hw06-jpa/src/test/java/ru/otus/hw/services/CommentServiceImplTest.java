package ru.otus.hw.services;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import ru.otus.hw.converters.CommentConverter;
import ru.otus.hw.repositories.BookRepositoryImpl;
import ru.otus.hw.repositories.CommentRepositoryImpl;

import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

@DataJpaTest
@Import({CommentServiceImpl.class, CommentRepositoryImpl.class, BookRepositoryImpl.class, CommentConverter.class})
class CommentServiceImplTest {

    @Autowired
    private CommentService service;

    @Autowired
    private CommentConverter converter;

    @Test
    void shouldNoLazyInitializationExceptionUponFindOneComment() {
        var throwable = catchThrowable(() -> service.findById(1L).map(converter::commentToString));
        assertThat(throwable).isNull();
    }

    @Test
    void shouldNoLazyInitializationExceptionUponFindCommentsOfBook() {
        var throwable = catchThrowable(() -> service.findByBookId(1L).stream()
                .map(converter::commentToString).collect(Collectors.joining()));
        assertThat(throwable).isNull();
    }

    @Test
    void shouldNoLazyInitializationExceptionUponInsertComment() {
        var throwable = catchThrowable(() ->
                converter.commentToString(service.insert("newText", 1L)));
        assertThat(throwable).isNull();
    }

    @Test
    void shouldNoLazyInitializationExceptionUponUpdateComment() {
        var throwable = catchThrowable(() ->
                converter.commentToString(service.update(2L, "newText")));
        assertThat(throwable).isNull();
    }
}