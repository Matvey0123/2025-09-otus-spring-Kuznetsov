package ru.otus.hw.services;

import ru.otus.hw.dtos.CommentDto;

import java.util.List;

public interface CommentService {

    List<CommentDto> findByBookId(long bookId);
}
