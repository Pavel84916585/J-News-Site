package RBPO.RBPO.services;

import RBPO.RBPO.entity.Article;
import RBPO.RBPO.repositories.ArticleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ArticleService {
    private final ArticleRepository articleRepository;

    public List<Article> listArticles(String title) {
        if (title != null) return articleRepository.findByTitle(title);
        return articleRepository.findAll();
    }
    public void saveArticle(Article article) {
        log.info("Saving new {}", article);
        articleRepository.save(article);
    }
    public void deleteArticle(Long id) {
        articleRepository.deleteById(id);
    }
    public Article getArticleById(long id) {
        return articleRepository.findById(id).orElse(null);
    }
}
