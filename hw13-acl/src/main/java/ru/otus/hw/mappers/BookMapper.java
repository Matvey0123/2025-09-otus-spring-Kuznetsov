package ru.otus.hw.mappers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.dtos.BookDto;
import ru.otus.hw.models.Book;

@Component
@RequiredArgsConstructor
public class BookMapper {

    private final AuthorMapper authorMapper;

    private final GenreMapper genreMapper;

    public BookDto mapToDto(Book book) {
        var author = authorMapper.mapToDto(book.getAuthor());
        var genres = book.getGenres().stream()
                .map(genreMapper::mapToDto)
                .toList();
        return new BookDto(book.getId(), book.getTitle(), author, genres);
    }

}
