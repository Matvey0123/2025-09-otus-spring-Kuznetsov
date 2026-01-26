package ru.otus.hw.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PagesController {

    @GetMapping("/authors")
    public String addAuthorsPage() {
        return "authors";
    }

    @GetMapping("/genres")
    public String addGenresPage() {
        return "genres";
    }

    @GetMapping("/")
    public String addBooksPage() {
        return "books";
    }

    @GetMapping("/comments")
    public String addCommentsPage() {
        return "comments";
    }

    @GetMapping("/books/edit")
    public String addBooksEditPage() {
        return "book-edit";
    }

    @GetMapping("/books/add")
    public String addBooksAddPage() {
        return "book-add";
    }

}
