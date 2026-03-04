package ru.otus.hw.mappers;

import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;
import ru.otus.hw.dtos.GenreDto;

import java.util.Locale;

@Component
public class GenreFormatter implements Formatter<GenreDto> {

    @Override
    public GenreDto parse(String id, Locale locale) {
        var genre = new GenreDto();
        genre.setId(Long.parseLong(id));
        return genre;
    }

    @Override
    public String print(GenreDto genreDto, Locale locale) {
        return genreDto.getName();
    }
}
