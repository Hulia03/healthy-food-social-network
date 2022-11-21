package com.telerikacademy.healthy.food.social.network.services;

import com.telerikacademy.healthy.food.social.network.exceptions.DuplicateEntityException;
import com.telerikacademy.healthy.food.social.network.exceptions.EntityNotFoundException;
import com.telerikacademy.healthy.food.social.network.models.Category;
import com.telerikacademy.healthy.food.social.network.repositories.contracts.CategoriesRepository;
import com.telerikacademy.healthy.food.social.network.services.contracts.CategoriesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

import static com.telerikacademy.healthy.food.social.network.utils.GlobalConstants.CATEGORY;
import static com.telerikacademy.healthy.food.social.network.utils.GlobalConstants.NAME;

@Service
public class CategoriesServiceImpl implements CategoriesService {
    private final CategoriesRepository categoriesRepository;

    @Autowired
    public CategoriesServiceImpl(CategoriesRepository categoriesRepository) {
        this.categoriesRepository = categoriesRepository;
    }

    @Override
    public Collection<Category> getAll() {
        return categoriesRepository.findAll();
    }

    @Override
    public Category getCategoryById(int id) {
        return categoriesRepository.findById(id)
                .orElseThrow(
                        () -> new EntityNotFoundException(CATEGORY, id));
    }

    @Override
    @Transactional
    public Category createCategory(Category category) {
        throwExceptionWhenCategoryNameExist(category.getCategory());
        return categoriesRepository.save(category);
    }

    @Override
    @Transactional
    public Category updateCategory(Category category) {
        throwExceptionWhenCategoryNotExist(category.getId());
        throwExceptionWhenCategoryWithNewNameExist(category.getId(), category.getCategory());
        return categoriesRepository.save(category);
    }

    @Override
    @Transactional
    public void deleteCategory(int id) {
        throwExceptionWhenCategoryNotExist(id);
        categoriesRepository.deleteById(id);
    }

    private void throwExceptionWhenCategoryNotExist(int id) {
        if (!categoriesRepository.existsById(id)) {
            throw new EntityNotFoundException(CATEGORY, id);
        }
    }

    private void throwExceptionWhenCategoryNameExist(String category) {
        if (categoriesRepository.findByCategory(category).isPresent()) {
            throw new DuplicateEntityException(CATEGORY, NAME, category);
        }
    }

    private void throwExceptionWhenCategoryWithNewNameExist(int id, String category) {
        boolean categoryChanged = !categoriesRepository.existsByIdAndCategory(id, category);
        boolean categoryExist = categoriesRepository.findByCategory(category).isPresent();
        if (categoryChanged && categoryExist) {
            throw new DuplicateEntityException(CATEGORY, NAME, category);
        }
    }
}
