package RBPO.RBPO.repositories;

import RBPO.RBPO.entity.Article;
import RBPO.RBPO.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {
    List<Article> findByTitle(String title);
    List<Article> findByCategory(Category category);

}
