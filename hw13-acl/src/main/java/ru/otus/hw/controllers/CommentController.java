package ru.otus.hw.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.otus.hw.services.CommentService;

@Controller
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/comments")
    public String getCommentsByBookId(@RequestParam("bookId") long bookId, Model model) {
        var comments = commentService.findByBookId(bookId).stream()
                .toList();
        model.addAttribute("comments", comments);
        return "comments";
    }
}
