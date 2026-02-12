package ru.otus.hw.controllers;

import org.springframework.test.context.bean.override.mockito.MockitoBean;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;
import ru.otus.hw.repositories.GenreRepository;

abstract class MockedRepositories {

    @MockitoBean
    protected AuthorRepository authorRepository;

    @MockitoBean
    protected GenreRepository genreRepository;

    @MockitoBean
    protected CommentRepository commentRepository;

    @MockitoBean
    protected BookRepository bookRepository;
}
