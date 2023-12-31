package RBPO.RBPO.repositories;

import RBPO.RBPO.entity.Article;
import RBPO.RBPO.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
    Optional<Image> findByName(String name);
    List<Optional<Image>> findByArticle(Article article);
}
