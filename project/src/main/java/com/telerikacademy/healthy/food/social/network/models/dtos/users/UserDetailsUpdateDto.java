package com.telerikacademy.healthy.food.social.network.models.dtos.users;

import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

import static com.telerikacademy.healthy.food.social.network.utils.GlobalConstants.*;

public class UserDetailsUpdateDto {
    private String email;

    @Size(min = USER_NAME_MIN_LENGTH,
            max = USER_NAME_MAX_LENGTH,
            message = USER_FIRST_NAME_MESSAGE_ERROR)
    private String firstName;

    @Size(min = USER_NAME_MIN_LENGTH,
            max = USER_NAME_MAX_LENGTH,
            message = USER_LAST_NAME_MESSAGE_ERROR)
    private String lastName;

    @Positive(message = VISIBILITY_REQUIRED_MESSAGE_ERROR)
    private int visibilityId;

    private MultipartFile picture;

    private String pictureUrl;

    @Size(max = USER_DESCRIPTION_MAX_LENGTH,
            message = USER_DESCRIPTION_MESSAGE_ERROR)
    private String description;

    @Min(value = 0, message = USER_AGE_MESSAGE_ERROR)
    private int age;

    private int genderId;

    private int nationalityId;

    public UserDetailsUpdateDto() {
        // Empty constructor
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getVisibilityId() {
        return visibilityId;
    }

    public void setVisibilityId(int visibilityId) {
        this.visibilityId = visibilityId;
    }

    public MultipartFile getPicture() {
        return picture;
    }

    public void setPicture(MultipartFile picture) {
        this.picture = picture;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getGenderId() {
        return genderId;
    }

    public void setGenderId(int genderId) {
        this.genderId = genderId;
    }

    public int getNationalityId() {
        return nationalityId;
    }

    public void setNationalityId(int nationalityId) {
        this.nationalityId = nationalityId;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }
}
