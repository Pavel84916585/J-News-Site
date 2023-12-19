package RBPO.RBPO.controllers;

import RBPO.RBPO.entity.Article;
import RBPO.RBPO.services.AppUserService;
import RBPO.RBPO.services.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MainController {
    private final ArticleService articleService;
    @GetMapping("/")
    public String qwe(Model model) {
        return "redirect:/all";
    }
    @GetMapping("/error")
    public String error() {
        return "error";
    }
    @GetMapping("/all")
    public String home(@RequestParam(name = "title", required = false) String title, Model model) {
        //model.addAttribute("articles", articleService.listArticles(title));
        List<Article> articles = articleService.getAllArticles();
        model.addAttribute("articles", articles);
        return "home";
    }
    @GetMapping("/profile")
    public String profile() {
        return "Profile";
    }
}
