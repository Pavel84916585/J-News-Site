package RBPO.RBPO.controllers;

import RBPO.RBPO.entity.Article;
import RBPO.RBPO.entity.Category;
import RBPO.RBPO.services.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;



    @PostMapping("/category/create")
    public String createCategory(Category category) {
        categoryService.saveCategory(category);
        return "redirect:/";
    }

    @GetMapping("/category/create")
    public String getCreateCategory(Model model) {
        if (!(model.containsAttribute("name") ))
            model.addAttribute("Category", new Category());

        return "CategoryCreate";
    }


    @PostMapping("/category/delete/{id}")
    public String deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return "redirect:/";
    }

}
