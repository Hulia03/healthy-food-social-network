package com.telerikacademy.healthy.food.social.network.models.dtos.users;

import com.telerikacademy.healthy.food.social.network.annotations.Match;
import com.telerikacademy.healthy.food.social.network.annotations.Password;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.*;

import static com.telerikacademy.healthy.food.social.network.utils.GlobalConstants.*;

@Match(first = "password", second = "passwordConfirmation", message = PASSWORD_DOESN_T_MATCH)
public class UserRegisterDto {
    @NotBlank(message = USER_EMAIL_MESSAGE_ERROR)
    @Email(message = USER_EMAIL_MESSAGE_ERROR)
    private String username;

    @Password(message = USER_PASSWORD_MESSAGE_ERROR)
    private String password;

    private String passwordConfirmation;

    @Size(min = USER_NAME_MIN_LENGTH,
            max = USER_NAME_MAX_LENGTH,
            message = USER_FIRST_NAME_MESSAGE_ERROR)
    private String firstName;

    @Size(min = USER_NAME_MIN_LENGTH,
            max = USER_NAME_MAX_LENGTH,
            message = USER_LAST_NAME_MESSAGE_ERROR)
    private String lastName;

    @Size(max = USER_DESCRIPTION_MAX_LENGTH,
            message = USER_DESCRIPTION_MESSAGE_ERROR)
    private String description;

    @Min(value = 0, message = USER_AGE_MESSAGE_ERROR)
    private int age;

    private int nationalityId;

    private int genderId;

    private MultipartFile picture;

    @Positive(message = VISIBILITY_REQUIRED_MESSAGE_ERROR)
    private int visibilityId;

    public UserRegisterDto() {
        // Empty constructor
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordConfirmation() {
        return passwordConfirmation;
    }

    public void setPasswordConfirmation(String passwordConfirmation) {
        this.passwordConfirmation = passwordConfirmation;
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

    public int getNationalityId() {
        return nationalityId;
    }

    public void setNationalityId(int nationalityId) {
        this.nationalityId = nationalityId;
    }

    public int getGenderId() {
        return genderId;
    }

    public void setGenderId(int genderId) {
        this.genderId = genderId;
    }

    public MultipartFile getPicture() {
        return picture;
    }

    public void setPicture(MultipartFile picture) {
        this.picture = picture;
    }

    public int getVisibilityId() {
        return visibilityId;
    }

    public void setVisibilityId(int visibilityId) {
        this.visibilityId = visibilityId;
    }
}
