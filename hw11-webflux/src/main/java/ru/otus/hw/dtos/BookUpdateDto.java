package ru.otus.hw.dtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookUpdateDto {

    private String id;

    @NotEmpty(message = "title must not be empty")
    private String title;

    @NotNull(message = "author must not be null")
    private String authorId;

    @NotEmpty(message = "genres must not be empty")
    private Set<String> genresIds;
}
