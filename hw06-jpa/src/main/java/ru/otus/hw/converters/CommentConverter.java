package ru.otus.hw.converters;

import org.springframework.stereotype.Component;
import ru.otus.hw.dtos.CommentDto;
import ru.otus.hw.models.Comment;

@Component
public class CommentConverter {

    public String commentToString(CommentDto comment) {
        return "Id: %d, Text: %s".formatted(comment.getId(), comment.getText());
    }

    public CommentDto commentToDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .build();
    }
}
