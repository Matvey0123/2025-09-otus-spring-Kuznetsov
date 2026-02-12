package ru.otus.hw.mappers;

import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;
import ru.otus.hw.dtos.AuthorDto;

import java.util.Locale;

@Component
public class AuthorFormatter implements Formatter<AuthorDto> {

    @Override
    public AuthorDto parse(String id, Locale locale) {
        var author = new AuthorDto();
        author.setId(id);
        return author;
    }

    @Override
    public String print(AuthorDto authorDto, Locale locale) {
        return authorDto.getFullName();
    }
}
