package RBPO.RBPO.services;

import RBPO.RBPO.entity.Article;
import RBPO.RBPO.repositories.ArticleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ArticleService {

    /*  Раскоментить низ */
    /*
    private final ArticleRepository articleRepository;

    public List<Article> listArticles(String title) {
        if (title != null) return articleRepository.findByTitle(title);
        return articleRepository.findAll();
    }
    public boolean saveArticle(Article article) {
        log.info("Saving new {}", article);
        articleRepository.save(article);
        return true;
    }
    public void deleteArticle(Long id) {
        articleRepository.deleteById(id);
    }
    public Article getArticleById(long id) {
        return articleRepository.findById(id).orElse(null);
    }


    */

    //Кирилл
    private final ArticleRepository articleRepository;

    @Autowired
    public ArticleService(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    public List<Article> getAllArticles() {
        return articleRepository.findAll();
    }

    public Article getArticleById(Long id) {
        return articleRepository.findById(id).orElse(null);
    }

    public void saveArticle(Article article) {
        articleRepository.save(article);
    }

    public void deleteArticle(Long id) {
        articleRepository.deleteById(id);
    }
}
