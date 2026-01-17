package ru.otus.hw.services;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.dtos.BookCreateDto;
import ru.otus.hw.dtos.BookDto;
import ru.otus.hw.dtos.BookUpdateDto;

public interface BookService {
    Mono<BookDto> findById(String id);

    Flux<BookDto> findAll();

    Mono<BookDto> insert(BookCreateDto bookCreateDto);

    Mono<BookDto> update(BookUpdateDto bookUpdateDto);

    Mono<Void> deleteById(String id);
}
