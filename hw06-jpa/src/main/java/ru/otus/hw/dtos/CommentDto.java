package ru.otus.hw.dtos;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CommentDto {
    private long id;

    private String text;
}
