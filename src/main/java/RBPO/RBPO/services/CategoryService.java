package RBPO.RBPO.services;

import RBPO.RBPO.entity.AppUser;
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


    public Category getCategoryByName(String categoryName) {
        return categoryRepository.findByName(categoryName);
    }


    public List<Category> listCategories(String name) {
        return categoryRepository.findAll();
    }

    public Category saveCategory(Category category) {
        log.info("Saving new {}", category);
        categoryRepository.save(category);
        return category;
    }
    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }
    public Category getCategoryById(long id) {
        return categoryRepository.findById(id).orElse(null);
    }
}
