package ru.otus.hw.mappers;

import org.springframework.stereotype.Component;
import ru.otus.hw.dtos.AuthorDto;
import ru.otus.hw.models.Author;

@Component
public class AuthorMapper {

    public AuthorDto mapToDto(Author source) {
        return new AuthorDto(source.getId(), source.getFullName());
    }

}
