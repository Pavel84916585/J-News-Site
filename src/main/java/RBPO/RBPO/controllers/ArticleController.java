package RBPO.RBPO.controllers;

import RBPO.RBPO.entity.AppUser;
import RBPO.RBPO.entity.Article;
import RBPO.RBPO.entity.Image;
import RBPO.RBPO.repositories.AppUserRepository;
import RBPO.RBPO.services.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequiredArgsConstructor
public class ArticleController {
    private final ArticleService articleService;
    private final AppUserRepository appUserRepository;
    @GetMapping("article/{id}")
    public String detailedArticle(@PathVariable long id, Model model) {
        model.addAttribute("article", articleService.getArticleById(id));
        return "detailedArticle";
    }


    @GetMapping("article/create")
    public  String getArticleCreationPage(Model model) {
        return "create";
    }
    @PostMapping("article/create")
//    public String createArticle(Model model,@RequestParam(name = "article", required = true)  Article article,@RequestParam(name = "appUser", required = false) AppUser appUser,@RequestParam(name = "file1", required = false) MultipartFile file1,@RequestParam(name = "file2", required = false) MultipartFile file2,@RequestParam(name = "file3", required = false) MultipartFile file3) throws IOException {
    public String createArticle() {
        Article article = new Article();
        article.setTitle("qwe");
        article.setText("asd");
        AppUser appUser = new AppUser();
        appUser.setPasswordHash("123qwe");
        appUser.setEmail("admin@admin");
        appUser.setUsername("admin");
        appUser.setActive(2);
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
        appUser = appUserRepository.findByEmail(appUser.getEmail());
        appUser.addArticleToUser(article);
        article.setAuthor(appUser);
//        article.setPreviewImageId(image1.getId());
        articleService.saveArticle(article);
        System.out.println(article);

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
