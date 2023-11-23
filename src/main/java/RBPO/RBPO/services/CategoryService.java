package RBPO.RBPO.services;

import RBPO.RBPO.entity.Article;
import RBPO.RBPO.entity.Category;
import RBPO.RBPO.repositories.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryService {
    private final CategoryRepository categoryRepository;
    public List<Category> listCategories(String name) {
        if (name != null) return categoryRepository.findByName(name);
        return categoryRepository.findAll();
    }

    public void saveCategory(Category category) {
        log.info("Saving new {}", category);
        categoryRepository.save(category);
    }
    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }
    public Category getCategoryById(long id) {
        return categoryRepository.findById(id).orElse(null);
    }
}
