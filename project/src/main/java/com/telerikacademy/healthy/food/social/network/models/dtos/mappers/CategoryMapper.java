package com.telerikacademy.healthy.food.social.network.models.dtos.mappers;

import com.telerikacademy.healthy.food.social.network.models.Category;
import com.telerikacademy.healthy.food.social.network.models.dtos.categories.CategoryDto;
import com.telerikacademy.healthy.food.social.network.services.contracts.FilesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.telerikacademy.healthy.food.social.network.models.dtos.mappers.Mapper.getNotNull;

@Component
public class CategoryMapper {
    private static final int EMOJI_WIDTH = 100;

    private final FilesService filesService;

    @Autowired
    public CategoryMapper(FilesService filesService) {
        this.filesService = filesService;
    }

    public Category toCategory(CategoryDto dto) {
        Category category = new Category();
        category.setCategory(dto.getCategory());
        category.setEmoji(filesService.uploadFile(dto.getFile(), EMOJI_WIDTH));
        return category;
    }

    public CategoryDto toCategoryDto(Category category) {
        CategoryDto dto = new CategoryDto();
        dto.setCategory(category.getCategory());
        dto.setFileUrl(category.getEmoji());
        return dto;
    }

    public Category mergeCategory(Category oldCategory, CategoryDto dto) {
        oldCategory.setCategory(getNotNull(oldCategory.getCategory(), dto.getCategory()));

        if (dto.getFile().isEmpty()) {
            oldCategory.setEmoji(oldCategory.getEmoji());
        } else {
            oldCategory.setEmoji(filesService.uploadFile(dto.getFile(), EMOJI_WIDTH));
        }

        return oldCategory;
    }
}
