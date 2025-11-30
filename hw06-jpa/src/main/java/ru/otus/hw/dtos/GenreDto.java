package ru.otus.hw.dtos;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class GenreDto {
    private long id;

    private String name;
}
