package com.telerikacademy.healthy.food.social.network.controllers.rest;

import com.telerikacademy.healthy.food.social.network.models.Category;
import com.telerikacademy.healthy.food.social.network.models.dtos.categories.CategoryDto;
import com.telerikacademy.healthy.food.social.network.models.dtos.mappers.CategoryMapper;
import com.telerikacademy.healthy.food.social.network.services.contracts.CategoriesService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;

import static com.telerikacademy.healthy.food.social.network.utils.GlobalConstants.*;

@RestController
@RequestMapping(API_URL + ADMIN_URL + CATEGORY_URL)
public class CategoriesRestController {
    private final CategoriesService categoriesService;
    private final CategoryMapper categoryMapper;

    @Autowired
    public CategoriesRestController(CategoriesService categoriesService, CategoryMapper categoryMapper) {
        this.categoriesService = categoriesService;
        this.categoryMapper = categoryMapper;
    }

    @GetMapping
    @ApiOperation(value = "List of all categories", response = Collection.class)
    public Collection<Category> getCategories() {
        return categoriesService.getAll();
    }

    @PostMapping
    @ApiOperation(value = "Create category", response = Category.class)
    public Category createCategory(@RequestPart MultipartFile file,
                                   @RequestPart("category") CategoryDto dto) {
        dto.setFile(file);
        Category category = categoryMapper.toCategory(dto);
        return categoriesService.createCategory(category);
    }

    @PutMapping("/{id}")
    @ApiOperation(value = "Update category", response = Category.class)
    public Category updateCategory(@PathVariable int id,
                                   @RequestPart(required = false) MultipartFile file,
                                   @RequestPart("category") CategoryDto dto) {
        dto.setFile(file);
        Category oldCategory = categoriesService.getCategoryById(id);
        Category category = categoryMapper.mergeCategory(oldCategory, dto);
        return categoriesService.updateCategory(category);
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Delete category", notes = "Return list of all categories", response = Collection.class)
    public Collection<Category> deleteCategory(@PathVariable int id) {
        categoriesService.deleteCategory(id);
        return categoriesService.getAll();
    }
}
