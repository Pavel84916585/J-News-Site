package RBPO.RBPO.controllers;

import RBPO.RBPO.entity.*;
import RBPO.RBPO.services.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ArticleController {
    private final AppUserService userService;

    private final ArticleService articleService;
    private final CategoryService categoryService;
    private final CommentService commentService;

    private final ImageService imageService;

    @GetMapping("article/{id}")
    public String detailedArticle(@PathVariable long id, Model model) {
        if (!(model.containsAttribute("Comment"))) {
            model.addAttribute("Comment", new Comment());
        }

        if (!(model.containsAttribute("Images"))) {
            model.addAttribute("Images", new Image());
        }



        //System.out.println(Base64.getEncoder().encodeToString(imageData));

        //System.out.println(imageService.downloadImage(articleService.getArticleById(id)));

        model.addAttribute("Images", imageService.downloadImage(articleService.getArticleById(id)));
        model.addAttribute("article", articleService.getArticleById(id));
        model.addAttribute("Comments", commentService.listComments(articleService.getArticleById(id)));

        return "detailedArticle";
    }




    @GetMapping("article/create")
    public  String getArticleCreationPage(Model model) {
        if (!(model.containsAttribute("title") || model.containsAttribute("text"))) {
            model.addAttribute("Article", new Article());
            model.addAttribute("Category", new Category());
        }
        return "create";
    }

    @GetMapping("/article/show")
    public String getArticleShowPage(Model model){
        List<Article> articles = articleService.getAllArticles();
        model.addAttribute("articles", articles);
        //System.out.println(articles);
        return "ArticleShow";
    }



    @PostMapping("/article/create")
    public String createArticle(@ModelAttribute("Article") Article article, @RequestParam("categoryName") String categoryName, @RequestParam("files") MultipartFile[] files, Model model) throws IOException {

        Category category = (Category) categoryService.getCategoryByName(categoryName);

        //проверяем есть ли указанная категория, если ее нет - создаем
        if (category == null) {
            category = new Category();
            category.setName(categoryName);
            categoryService.saveCategory(category);
        }


       // String uploadImage = ImageService.uploadImage(files);


        article.setCategory(category);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication(); // берем данные пользователя создавшего статью
        String currentPrincipalName = authentication.getName();

        // System.out.println(currentPrincipalName);

        AppUser user = (AppUser) this.userService.getAppUserByEmail(currentPrincipalName);

        article.setAuthor(user);


        articleService.saveArticle(article);


        //сохранение картинки для статьи
        String uploadImage = null;
        for (MultipartFile file : files) {

            uploadImage = imageService.uploadImage(file, article);
        }

        //       Никакого сегодня СТРИНГА НЕ БУДЕТ.  👈(ﾟヮﾟ👈)

        return "home";
    }


    @PostMapping("/article/delete/{id}")
    public String deleteArticle(@PathVariable Long id) {
        articleService.deleteArticle(id);
        return "redirect:/all";
    }
}
