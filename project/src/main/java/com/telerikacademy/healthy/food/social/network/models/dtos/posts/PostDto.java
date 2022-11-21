package com.telerikacademy.healthy.food.social.network.models.dtos.posts;

import com.telerikacademy.healthy.food.social.network.models.MediaType;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.util.Set;

import static com.telerikacademy.healthy.food.social.network.utils.GlobalConstants.*;

public class PostDto {
    @Size(min = POST_TITLE_MIN_LEN,
            max = POST_TITLE_MAX_LEN,
            message = POST_TITLE_MESSAGE_ERROR)
    private String title;

    private String description;

    @Positive(message = VISIBILITY_REQUIRED_MESSAGE_ERROR)
    private int visibilityId;

    private MultipartFile file;

    private MediaType mediaType;

    private String fileUrl;

    private Set<Integer> categories;

    public PostDto() {
        // Empty constructor
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getVisibilityId() {
        return visibilityId;
    }

    public void setVisibilityId(int visibilityId) {
        this.visibilityId = visibilityId;
    }

    public Set<Integer> getCategories() {
        return categories;
    }

    public void setCategories(Set<Integer> categories) {
        this.categories = categories;
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

    public void setFileUrl(String pictureUrl) {
        this.fileUrl = pictureUrl;
    }

    public MediaType getMediaType() {
        return mediaType;
    }

    public void setMediaType(MediaType mediaType) {
        this.mediaType = mediaType;
    }
}
