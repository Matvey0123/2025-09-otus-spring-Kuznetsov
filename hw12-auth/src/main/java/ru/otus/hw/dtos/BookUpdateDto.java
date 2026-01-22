package ru.otus.hw.dtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookUpdateDto {

    private long id;

    @NotEmpty(message = "title must not be empty")
    private String title;

    @NotNull(message = "author must not be null")
    private AuthorDto author;

    @NotEmpty(message = "genres must not be empty")
    private List<GenreDto> genres;
}
