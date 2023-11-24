package RBPO.RBPO.controllers;

import RBPO.RBPO.entity.Article;
import RBPO.RBPO.services.AppUserService;
import RBPO.RBPO.services.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class ArticleController {
    private final ArticleService articleService;
    @GetMapping("/")
    public String qwe(Model model) {
        
        return "qwe";
    }

    @GetMapping("/all")
    public String home(@RequestParam(name = "title", required = false) String title, Model model) {
        model.addAttribute("articles", articleService.listArticles(title));
        return "home";
    }
    @GetMapping("/article/{id}")
    public String detailedArticle(@PathVariable long id, Model model) {
        model.addAttribute("article", articleService.getArticleById(id));
        return "detailedArticle";
    }
    @PostMapping("/article/create")
    public String createArticle(Article article) {
        articleService.saveArticle(article);
        return "redirect:/";
    }
    @PostMapping("/article/delete/{id}")
    public String deleteArticle(@PathVariable Long id) {
        articleService.deleteArticle(id);
        return "redirect:/";
    }
}
