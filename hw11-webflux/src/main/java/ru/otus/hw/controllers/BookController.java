package ru.otus.hw.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.dtos.BookCreateDto;
import ru.otus.hw.dtos.BookDto;
import ru.otus.hw.dtos.BookUpdateDto;
import ru.otus.hw.services.BookService;

@RestController
@RequestMapping("/api/v1/book")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @GetMapping
    public Flux<BookDto> getAllBooks() {
        return bookService.findAll();
    }

    @GetMapping("/{id}")
    public Mono<BookDto> getBook(@PathVariable("id") String id) {
        return bookService.findById(id);
    }

    @PatchMapping("/{id}")
    public Mono<ResponseEntity<BookDto>> editBook(@PathVariable("id") String id,
                                                  @Valid @RequestBody BookUpdateDto bookUpdateDto) {
        return bookService.update(bookUpdateDto)
                .map(ResponseEntity::ok);
    }

    @PostMapping
    public Mono<ResponseEntity<BookDto>> addBook(@Valid @RequestBody BookCreateDto bookCreateDto) {
        return bookService.insert(bookCreateDto)
                .map(b -> ResponseEntity.status(HttpStatus.CREATED).body(b));
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteBook(@PathVariable("id") String id) {
        return bookService.deleteById(id)
                .thenReturn(ResponseEntity.status(HttpStatus.NO_CONTENT).build());
    }
}
