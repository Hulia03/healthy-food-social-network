package com.telerikacademy.healthy.food.social.network.services;

import com.telerikacademy.healthy.food.social.network.exceptions.DuplicateEntityException;
import com.telerikacademy.healthy.food.social.network.exceptions.EntityNotFoundException;
import com.telerikacademy.healthy.food.social.network.models.Category;
import com.telerikacademy.healthy.food.social.network.repositories.contracts.CategoriesRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

import static com.telerikacademy.healthy.food.social.network.Factory.createCategoryVegan;
import static com.telerikacademy.healthy.food.social.network.Factory.createCategoryVegetables;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.times;

@RunWith(MockitoJUnitRunner.class)
public class CategoriesServiceImplTests {
    @Mock
    CategoriesRepository mockRepository;

    @InjectMocks
    CategoriesServiceImpl mockService;

    @Test
    public void getAllShould_ReturnListOfCategories() {
        // Arrange
        Mockito.when(mockRepository.findAll())
                .thenReturn(Arrays.asList(createCategoryVegetables()));

        // Act
        Collection<Category> list = mockService.getAll();

        // Assert
        Assert.assertEquals(1, list.size());
    }

    @Test
    public void getAllShould_ReturnEmptyListOfCategories() {
        // Arrange
        Mockito.when(mockRepository.findAll())
                .thenReturn(Collections.emptyList());

        // Act
        Collection<Category> list = mockService.getAll();

        // Assert
        Assert.assertEquals(0, list.size());
    }

    @Test
    public void getCategoryByIdShould_ReturnCategory_WhenCategoryExists() {
        // Arrange
        Category expected = createCategoryVegetables();

        Mockito.when(mockRepository.findById(anyInt()))
                .thenReturn(Optional.of(expected));

        // Act
        Category actual = mockService.getCategoryById(anyInt());

        // Assert
        Assert.assertSame(expected, actual);
    }

    @Test
    public void getCategoryByIdShould_Throw_WhenCategoryNotExists() {
        // Arrange, Act, Assert
        Assert.assertThrows(EntityNotFoundException.class,
                () -> mockService.getCategoryById(anyInt()));
    }

    @Test
    public void getCategoryByIdShould_CallRepository() {
        // Arrange
        Category expected = createCategoryVegetables();

        Mockito.when(mockRepository.findById(anyInt()))
                .thenReturn(Optional.of(expected));

        // Act
        mockService.getCategoryById(anyInt());

        // Assert
        Mockito.verify(mockRepository,
                times(1)).findById(anyInt());

    }

    @Test
    public void createCategoryShould_ReturnCategory() {
        // Arrange
        Category category = createCategoryVegetables();

        Mockito.when(mockRepository.findByCategory(anyString()))
                .thenReturn(Optional.empty());

        Mockito.when(mockRepository.save(any(Category.class))).thenReturn(category);

        // Act
        Category actual = mockService.createCategory(category);

        // Assert
        Assert.assertSame(category, actual);
    }

    @Test
    public void createCategoryShould_Throw_WhenCategoryAlreadyExist() {
        // Arrange
        Category category = createCategoryVegetables();
        Mockito.when(mockRepository.findByCategory(anyString()))
                .thenReturn(Optional.of(category));

        // Act, Assert
        Assert.assertThrows(DuplicateEntityException.class,
                () -> mockService.createCategory(category));
    }

    @Test
    public void updateCategoryShould_ReturnCategory() {
        // Arrange
        Mockito.when(mockRepository.existsById(anyInt()))
                .thenReturn(true);

        Category category = createCategoryVegetables();
        Mockito.when(mockRepository.save(any(Category.class))).thenReturn(category);

        // Act
        Category actual = mockService.updateCategory(category);

        // Assert
        Assert.assertSame(category, actual);
    }

    @Test
    public void updateCategoryShould_Throw_WhenCategoryNotExist() {
        // Arrange
        Mockito.when(mockRepository.existsById(anyInt()))
                .thenReturn(false);

        // Act, Assert
        Assert.assertThrows(EntityNotFoundException.class,
                () -> mockService.updateCategory(createCategoryVegetables()));
    }

    @Test
    public void updateCategoryShould_Throw_WhenCategoryNameExist() {
        // Arrange
        Category category = createCategoryVegetables();
        Category update = createCategoryVegan();
        Mockito.when(mockRepository.existsById(anyInt()))
                .thenReturn(true);
        Mockito.when(mockRepository.existsByIdAndCategory(anyInt(), anyString()))
                .thenReturn(false);

        Mockito.when(mockRepository.findByCategory(anyString()))
                .thenReturn(Optional.of(category));

        // Act, Assert
        Assert.assertThrows(DuplicateEntityException.class,
                () -> mockService.updateCategory(createCategoryVegetables()));
    }

    @Test
    public void deleteCategoryShould_Throw_WhenCategoryNotExist() {
        // Arrange
        Mockito.when(mockRepository.existsById(anyInt()))
                .thenReturn(false);

        // Act, Assert
        Assert.assertThrows(EntityNotFoundException.class,
                () -> mockService.deleteCategory(anyInt()));
    }

    @Test
    public void deleteCategoryShould_CallRepository() {
        // Arrange
        Mockito.when(mockRepository.existsById(anyInt()))
                .thenReturn(true);

        // Act
        mockService.deleteCategory(anyInt());

        // Assert
        Mockito.verify(mockRepository, times(1)).deleteById(anyInt());
    }
}

