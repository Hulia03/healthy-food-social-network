package com.telerikacademy.healthy.food.social.network.services.contracts;

import com.telerikacademy.healthy.food.social.network.models.Category;

import java.util.Collection;


public interface CategoriesService {
    Collection<Category> getAll();

    Category getCategoryById(int id);

    Category createCategory(Category category);

    Category updateCategory(Category category);

    void deleteCategory(int id);
}
