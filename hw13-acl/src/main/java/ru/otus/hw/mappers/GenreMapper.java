package ru.otus.hw.mappers;

import org.springframework.stereotype.Component;
import ru.otus.hw.dtos.GenreDto;
import ru.otus.hw.models.Genre;

@Component
public class GenreMapper {

    public GenreDto mapToDto(Genre source) {
        return new GenreDto(source.getId(), source.getName());
    }

}
