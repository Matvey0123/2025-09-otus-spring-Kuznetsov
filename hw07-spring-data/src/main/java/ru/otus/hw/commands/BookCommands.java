package ru.otus.hw.commands;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.hw.converters.BookConverter;
import ru.otus.hw.models.Book;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.CommentService;

import java.util.Set;
import java.util.stream.Collectors;

@SuppressWarnings({"SpellCheckingInspection", "unused"})
@ShellComponent
@RequiredArgsConstructor
public class BookCommands {

    private final BookService bookService;

    private final BookConverter bookConverter;

    private final CommentService commentService;

    @ShellMethod(value = "Find all books", key = "ab")
    public String findAllBooks() {
        return bookService.findAll().stream()
                .map(this::convertToStringWithComments)
                .collect(Collectors.joining("," + System.lineSeparator()));
    }

    @ShellMethod(value = "Find book by id", key = "bbid")
    public String findBookById(long id) {
        return bookService.findById(id)
                .map(this::convertToStringWithComments)
                .orElse("Book with id %d not found".formatted(id));
    }

    // bins newBook 1 1,2
    @ShellMethod(value = "Insert book", key = "bins")
    public String insertBook(String title, long authorId, Set<Long> genresIds) {
        var savedBook = bookService.insert(title, authorId, genresIds);
        return convertToStringWithComments(savedBook);
    }

    // bupd 4 editedBook 3 2,3
    @ShellMethod(value = "Update book", key = "bupd")
    public String updateBook(long id, String title, long authorId, Set<Long> genresIds) {
        var savedBook = bookService.update(id, title, authorId, genresIds);
        return convertToStringWithComments(savedBook);
    }

    // bdel 4
    @ShellMethod(value = "Delete book by id", key = "bdel")
    public void deleteBook(long id) {
        bookService.deleteById(id);
    }

    private String convertToStringWithComments(Book book) {
        var comments = commentService.findByBookId(book.getId());
        return bookConverter.bookToString(book, comments);
    }
}
