package com.telerikacademy.healthy.food.social.network.controllers.mvc;

import com.telerikacademy.healthy.food.social.network.models.Category;
import com.telerikacademy.healthy.food.social.network.models.dtos.categories.CategoryDto;
import com.telerikacademy.healthy.food.social.network.models.dtos.mappers.CategoryMapper;
import com.telerikacademy.healthy.food.social.network.services.contracts.CategoriesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.telerikacademy.healthy.food.social.network.utils.GlobalConstants.*;

@Controller
@RequestMapping(ADMIN_URL + CATEGORY_URL)
public class CategoriesMvcController {
    private final CategoriesService categoriesService;
    private final CategoryMapper categoryMapper;

    @Autowired
    public CategoriesMvcController(CategoriesService categoriesService, CategoryMapper categoryMapper) {
        this.categoriesService = categoriesService;
        this.categoryMapper = categoryMapper;
    }

    @GetMapping
    public String showCategories(final Model model) {
        model.addAttribute("categories", categoriesService.getAll());
        return "admin/categories/categories";
    }

    @GetMapping("/new")
    public String showNewCategoryForm(final Model model) {
        model.addAttribute("category", new CategoryDto());
        return "admin/categories/create-category";
    }

    @PostMapping("/new")
    public String createCategory(@Valid @ModelAttribute("category") CategoryDto dto,
                                 BindingResult errors, final Model model) {
        if (errors.hasErrors()) {
            return "admin/categories/create-category";
        }
        if (dto.getFile().isEmpty()) {
            model.addAttribute("error", FILE_REQUIRED_MESSAGE_ERROR);
            return "admin/categories/create-category";
        }

        Category category = categoryMapper.toCategory(dto);
        categoriesService.createCategory(category);
        return "redirect:/admin/categories";
    }

    @GetMapping("/{id}/update")
    public String showUpdateCategoryForm(@PathVariable int id, final Model model) {
        Category category = categoriesService.getCategoryById(id);
        CategoryDto dto = categoryMapper.toCategoryDto(category);
        model.addAttribute("category", dto);
        return "admin/categories/update-category";
    }

    @PostMapping("/{id}/update")
    public String updateCategory(@PathVariable int id,
                                 @Valid @ModelAttribute("category") CategoryDto dto,
                                 BindingResult errors) {
        if (errors.hasErrors()) {
            return "admin/categories/update-category";
        }

        Category oldCategory = categoriesService.getCategoryById(id);
        Category category = categoryMapper.mergeCategory(oldCategory, dto);
        categoriesService.updateCategory(category);
        return "redirect:/admin/categories";
    }

    @GetMapping("/{id}/delete")
    public String showDeleteCategoryForm(@PathVariable int id, final Model model) {
        Category category = categoriesService.getCategoryById(id);
        model.addAttribute("category", category);
        return "admin/categories/delete-category";
    }

    @PostMapping("/{id}/delete")
    public String deleteCategory(@PathVariable int id) {
        categoriesService.deleteCategory(id);
        return "redirect:/admin/categories";
    }
}
