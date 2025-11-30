package ru.otus.hw.converters;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.dtos.BookDto;
import ru.otus.hw.dtos.CommentDto;
import ru.otus.hw.dtos.GenreDto;
import ru.otus.hw.models.Book;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class BookConverter {
    private final AuthorConverter authorConverter;

    private final GenreConverter genreConverter;

    private final CommentConverter commentConverter;

    public String bookToString(BookDto book) {
        var genresString = book.getGenres().stream()
                .map(genreConverter::genreToString)
                .map("{%s}"::formatted)
                .collect(Collectors.joining(", "));
        var commentsString = book.getComments().stream()
                .map(commentConverter::commentToString)
                .map("{%s}"::formatted)
                .collect(Collectors.joining(", "));
        return "Id: %d, title: %s, author: {%s}, genres: [%s], comments: [%s]".formatted(
                book.getId(),
                book.getTitle(),
                authorConverter.authorToString(book.getAuthor()),
                genresString,
                commentsString);
    }

    public BookDto bookToDto(Book book) {
        if (book == null) {
            return null;
        }
        List<GenreDto> genres = new ArrayList<>();
        if (book.getGenres() != null) {
            genres.addAll(book.getGenres().stream().map(genreConverter::genreToDto).toList());
        }
        List<CommentDto> comments = new ArrayList<>();
        if (book.getComments() != null) {
            comments.addAll(book.getComments().stream().map(commentConverter::commentToDto).toList());
        }
        return BookDto.builder()
                .id(book.getId())
                .title(book.getTitle())
                .author(authorConverter.authorToDto(book.getAuthor()))
                .genres(genres)
                .comments(comments)
                .build();
    }
}
