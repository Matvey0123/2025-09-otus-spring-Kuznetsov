package ru.otus.hw.converters;

import org.springframework.stereotype.Component;
import ru.otus.hw.dtos.GenreDto;
import ru.otus.hw.models.Genre;

@Component
public class GenreConverter {

    public String genreToString(GenreDto genre) {
        return "Id: %d, Name: %s".formatted(genre.getId(), genre.getName());
    }

    public GenreDto genreToDto(Genre genre) {
        return GenreDto.builder()
                .id(genre.getId())
                .name(genre.getName())
                .build();
    }
}
