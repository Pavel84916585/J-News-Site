package RBPO.RBPO.controllers;

import RBPO.RBPO.entity.Article;
import RBPO.RBPO.entity.Comment;
import RBPO.RBPO.services.AppUserService;
import RBPO.RBPO.services.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;
    private final AppUserService userService;
    @PostMapping("/comment/create")
    public String createComment(@ModelAttribute("Comment") Comment comment, @RequestParam("Article")Article article) {
        Comment savingComment = new Comment();
        savingComment.setCommentText(comment.getCommentText());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication(); // берем данные пользователя читающего статью
        String currentPrincipalName = authentication.getName();

        savingComment.setArticle(article);

        savingComment.setAuthor(this.userService.getAppUserByEmail(currentPrincipalName));
        commentService.saveComment(savingComment);
        article.addCommentToArticle(savingComment);
        return "redirect:/article";
    }
    @PostMapping("/comment/delete/{id}")
    public String deleteComment(@PathVariable Long id) {
        commentService.deleteComment(id);
        return "redirect:/";
    }
}
