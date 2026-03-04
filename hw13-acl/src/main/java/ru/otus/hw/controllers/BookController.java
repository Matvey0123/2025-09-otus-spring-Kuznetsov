package ru.otus.hw.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.otus.hw.dtos.BookCreateDto;
import ru.otus.hw.dtos.BookDto;
import ru.otus.hw.dtos.BookUpdateDto;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.GenreService;

@Controller
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    private final AuthorService authorService;

    private final GenreService genreService;

    @GetMapping("/")
    public String getAllBooks(Model model) {
        var books = bookService.findAll().stream()
                .toList();
        model.addAttribute("books", books);
        return "books";
    }

    @GetMapping("favicon.ico")
    @ResponseBody
    public void disableFavicon() {
    }

    @GetMapping("/books/edit")
    public String editBook(@RequestParam("id") long id, Model model) {
        var book = bookService.findById(id);
        var allAuthors = authorService.findAll().stream()
                .toList();
        var allGenres = genreService.findAll().stream()
                .toList();
        model.addAttribute("book", book);
        model.addAttribute("allAuthors", allAuthors);
        model.addAttribute("allGenres", allGenres);
        return "book-edit";
    }

    @PostMapping("/books/{id}")
    public String editBook(@Valid @ModelAttribute("book") BookUpdateDto bookUpdateDto) {
        bookService.update(bookUpdateDto);
        return "redirect:/";
    }

    @GetMapping("/books/add")
    public String addBook(Model model) {
        var allAuthors = authorService.findAll().stream()
                .toList();
        var allGenres = genreService.findAll().stream()
                .toList();
        model.addAttribute("book", new BookDto());
        model.addAttribute("allAuthors", allAuthors);
        model.addAttribute("allGenres", allGenres);
        return "book-add";
    }

    @PostMapping("/books")
    public String addBook(@Valid @ModelAttribute("book") BookCreateDto bookCreateDto) {
        bookService.insert(bookCreateDto);
        return "redirect:/";
    }

    @PostMapping("/books/delete")
    public String deleteBook(@RequestParam("id") long id) {
        bookService.deleteById(id);
        return "redirect:/";
    }
}
