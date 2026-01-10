package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.dtos.BookCreateDto;
import ru.otus.hw.dtos.BookDto;
import ru.otus.hw.dtos.BookUpdateDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.mappers.BookMapper;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;
import ru.otus.hw.repositories.GenreRepository;

import java.util.List;
import java.util.Set;

import static org.springframework.util.CollectionUtils.isEmpty;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    private final CommentRepository commentRepository;

    private final BookRepository bookRepository;

    private final BookMapper bookMapper;

    @Override
    @Transactional(readOnly = true)
    public BookDto findById(long id) {
        return bookRepository.findById(id)
                .map(bookMapper::mapToDto)
                .orElseThrow(jakarta.persistence.EntityNotFoundException::new);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookDto> findAll() {
        return bookRepository.findAll().stream()
                .map(bookMapper::mapToDto)
                .toList();
    }

    @Override
    @Transactional
    public BookDto insert(BookCreateDto dto) {
        var authorId = dto.getAuthorId();
        var genreIds = dto.getGenresIds();
        var author = findAuthor(authorId);
        var genres = findGenres(genreIds);
        var book = new Book(0, dto.getTitle(), author, genres);
        return bookMapper.mapToDto(bookRepository.save(book));
    }

    @Override
    @Transactional
    public BookDto update(BookUpdateDto dto) {
        var bookId = dto.getId();
        var authorId = dto.getAuthorId();
        var genresIds = dto.getGenresIds();
        var book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book with id %d not found".formatted(bookId)));
        var author = findAuthor(authorId);
        var genres = findGenres(genresIds);
        book.setTitle(dto.getTitle());
        book.setAuthor(author);
        book.setGenres(genres);
        return bookMapper.mapToDto(bookRepository.save(book));
    }

    @Override
    @Transactional
    public void deleteById(long id) {
        commentRepository.deleteByBookId(id);
        bookRepository.deleteById(id);
    }

    private Author findAuthor(long authorId) {
        return authorRepository.findById(authorId)
                .orElseThrow(() -> new EntityNotFoundException("Author with id %d not found".formatted(authorId)));
    }

    private List<Genre> findGenres(Set<Long> genresIds) {
        if (isEmpty(genresIds)) {
            throw new EntityNotFoundException("Genres are empty");
        }
        var genres = genreRepository.findAllById(genresIds);
        if (isEmpty(genres) || genresIds.size() != genres.size()) {
            throw new EntityNotFoundException("One or all genres with ids %s not found".formatted(genresIds));
        }
        return genres;
    }
}
