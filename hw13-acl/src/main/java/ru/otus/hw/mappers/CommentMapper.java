package ru.otus.hw.mappers;

import org.springframework.stereotype.Component;
import ru.otus.hw.dtos.CommentDto;
import ru.otus.hw.models.Comment;

@Component
public class CommentMapper {

    public CommentDto mapToDto(Comment source) {
        return new CommentDto(source.getId(), source.getText());
    }
}
