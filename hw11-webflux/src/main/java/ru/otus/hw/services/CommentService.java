package ru.otus.hw.services;

import reactor.core.publisher.Flux;
import ru.otus.hw.dtos.CommentDto;

public interface CommentService {

    Flux<CommentDto> findByBookId(String bookId);
}
