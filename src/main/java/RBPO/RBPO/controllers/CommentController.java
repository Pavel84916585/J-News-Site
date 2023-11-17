package RBPO.RBPO.controllers;

import RBPO.RBPO.entity.Comment;
import RBPO.RBPO.services.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;
    @PostMapping("/comment/create")
    public String createComment(Comment comment) {
        commentService.saveComment(comment);
        return "redirect:/";
    }
    @PostMapping("/comment/delete/{id}")
    public String deleteComment(@PathVariable Long id) {
        commentService.deleteComment(id);
        return "redirect:/";
    }
}
