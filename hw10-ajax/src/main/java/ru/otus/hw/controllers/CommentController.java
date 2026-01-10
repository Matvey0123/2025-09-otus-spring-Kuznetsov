package ru.otus.hw.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.dtos.CommentDto;
import ru.otus.hw.services.CommentService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/api/v1/book/{bookId}/comment")
    public List<CommentDto> getCommentsByBookId(@PathVariable("bookId") long bookId) {
        return commentService.findByBookId(bookId).stream().toList();
    }
}
