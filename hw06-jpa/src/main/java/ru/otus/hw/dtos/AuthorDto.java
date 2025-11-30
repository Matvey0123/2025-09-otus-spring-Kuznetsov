package ru.otus.hw.dtos;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class AuthorDto {
    private long id;

    private String fullName;
}
