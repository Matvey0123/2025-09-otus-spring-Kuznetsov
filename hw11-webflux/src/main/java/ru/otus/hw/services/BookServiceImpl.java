package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.dtos.BookCreateDto;
import ru.otus.hw.dtos.BookDto;
import ru.otus.hw.dtos.BookUpdateDto;
import ru.otus.hw.mappers.BookMapper;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.GenreRepository;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    private final BookRepository bookRepository;

    private final BookMapper bookMapper;

    @Override
    public Mono<BookDto> findById(String id) {
        return bookRepository.findById(id)
                .map(bookMapper::mapToDto);
    }

    @Override
    public Flux<BookDto> findAll() {
        return bookRepository.findAll()
                .map(bookMapper::mapToDto);
    }

    @Override
    public Mono<BookDto> insert(BookCreateDto bookCreateDto) {
        return Mono.just(new Book())
                .flatMap(book ->
                        Mono.zip(findAuthor(bookCreateDto.getAuthorId()), findGenres(bookCreateDto.getGenresIds()))
                                .flatMap(tuple -> {
                                    var author = tuple.getT1();
                                    var genres = tuple.getT2();

                                    book.setTitle(bookCreateDto.getTitle());
                                    book.setAuthor(author);
                                    book.setGenres(genres);
                                    return bookRepository.save(book).map(bookMapper::mapToDto);
                                })
                );
    }

    @Override
    public Mono<BookDto> update(BookUpdateDto bookUpdateDto) {
        return findBook(bookUpdateDto.getId())
                .flatMap(book ->
                        Mono.zip(findAuthor(bookUpdateDto.getAuthorId()), findGenres(bookUpdateDto.getGenresIds()))
                                .flatMap(tuple -> {
                                    var author = tuple.getT1();
                                    var genres = tuple.getT2();

                                    book.setTitle(bookUpdateDto.getTitle());
                                    book.setAuthor(author);
                                    book.setGenres(genres);
                                    return bookRepository.save(book).map(bookMapper::mapToDto);
                                }));
    }

    @Override
    public Mono<Void> deleteById(String id) {
        return Mono.just(id).flatMap(bookRepository::deleteById);
    }

    private Mono<Author> findAuthor(String authorId) {
        return authorRepository.findById(authorId);
    }

    private Mono<List<Genre>> findGenres(Set<String> genresIds) {
        return genreRepository.findAllById(genresIds).collectList();
    }

    private Mono<Book> findBook(String bookId) {
        return bookRepository.findById(bookId);
    }
}
