package RBPO.RBPO.controllers;

import RBPO.RBPO.entity.Comment;
import RBPO.RBPO.services.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/article/{id}")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;
    @PostMapping("/comment/create")
    public String createComment(@PathVariable long id, Comment comment) {
        commentService.saveComment(comment);
        return "redirect:/";
    }
    @PostMapping("/comment/delete/{idc}")
    public String deleteComment(@PathVariable Long idc) {
        commentService.deleteComment(idc);
        return "redirect:/";
    }
}
