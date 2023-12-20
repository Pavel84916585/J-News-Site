package RBPO.RBPO.controllers;

import RBPO.RBPO.entity.*;
import RBPO.RBPO.services.AppUserService;
import RBPO.RBPO.services.ArticleService;
import RBPO.RBPO.services.CategoryService;
import RBPO.RBPO.services.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ArticleController {
    private final AppUserService userService;

    private final ArticleService articleService;
    private final CategoryService categoryService;
    private final CommentService commentService;

    @GetMapping("article/{id}")
    public String detailedArticle(@PathVariable long id, Model model) {
        if (!(model.containsAttribute("Comment"))) {
            model.addAttribute("Comment", new Comment());
            System.out.println("123421254125121");
        }
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



    @PostMapping("article/create")
    public String createArticle(@ModelAttribute("Article") Article article, @RequestParam("categoryName") String categoryName, Model model){


        Category category = (Category) categoryService.getCategoryByName(categoryName);


        //проверяем есть ли указанная категория, если ее нет - создаем
        if(category == null){
            category = new Category();
            category.setName(categoryName);
            categoryService.saveCategory(category);
        }

        List<Image> images = new ArrayList<Image>();
        Image image1 = null;
        images.add(image1);




        article.setCategory(category);
        article.setImages(images);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication(); // берем данные пользователя создавшего статью
        String currentPrincipalName = authentication.getName();

       // System.out.println(currentPrincipalName);

        AppUser user = (AppUser) this.userService.getAppUserByEmail(currentPrincipalName);

        article.setAuthor(user);

       // System.out.println(currentPrincipalName);










//        Image image1 = null;
//        Image image2;
//        Image image3;
//        if (file1.getSize() != 0) {
//            image1 = toImageEntity(file1);
//            image1.setPreviewImage(true);
//            article.addImageToProductArticle(image1);
//        }
//        if (file2.getSize() != 0) {
//            image2 = toImageEntity(file2);
//            article.addImageToProductArticle(image2);
//        }
//        if (file3.getSize() != 0) {
//            image3 = toImageEntity(file3);
//            article.addImageToProductArticle(image3);
//        }



        articleService.saveArticle(article);


        //       Никакого сегодня СТРИНГА НЕ БУДЕТ. Дальше БОГА НЕТ 👈(ﾟヮﾟ👈)
        //System.out.println(article);

        return "redirect: /all";
    }



    private Image toImageEntity(MultipartFile file1) throws IOException {
        Image image = new Image();
        image.setName(file1.getName());
        image.setSize(file1.getSize());
        image.setBytes(file1.getBytes());
        image.setOriginalFileName(file1.getOriginalFilename());
        image.setContentType(file1.getContentType());
        return image;
    }

    @PostMapping("/article/delete/{id}")
    public String deleteArticle(@PathVariable Long id) {
        articleService.deleteArticle(id);
        return "redirect:/all";
    }
}
