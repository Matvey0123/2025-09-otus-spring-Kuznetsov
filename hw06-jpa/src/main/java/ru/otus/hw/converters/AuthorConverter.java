package ru.otus.hw.converters;

import org.springframework.stereotype.Component;
import ru.otus.hw.dtos.AuthorDto;
import ru.otus.hw.models.Author;

@Component
public class AuthorConverter {

    public String authorToString(AuthorDto author) {
        return "Id: %d, FullName: %s".formatted(author.getId(), author.getFullName());
    }

    public AuthorDto authorToDto(Author author) {
        return AuthorDto.builder()
                .id(author.getId())
                .fullName(author.getFullName())
                .build();
    }
}
