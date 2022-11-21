package com.telerikacademy.healthy.food.social.network.models.dtos.categories;

import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Size;

import static com.telerikacademy.healthy.food.social.network.utils.GlobalConstants.*;

public class CategoryDto {
    @Size(min = CATEGORY_NAME_MIN_LEN,
            max = CATEGORY_NAME_MAX_LEN,
            message = CATEGORY_NAME_MESSAGE_ERROR)
    private String category;

    private MultipartFile file;

    private String fileUrl;

    public CategoryDto() {
        // Empty constructor
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }
}
