package com.telerikacademy.healthy.food.social.network.models.dtos;

import com.telerikacademy.healthy.food.social.network.annotations.Match;
import com.telerikacademy.healthy.food.social.network.annotations.Password;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import static com.telerikacademy.healthy.food.social.network.utils.GlobalConstants.*;

@Match(first = "password", second = "passwordConfirmation", message = PASSWORD_DOESN_T_MATCH)
public class PasswordUserDto {
    @Email(message = USER_EMAIL_MESSAGE_ERROR)
    private String username;

    private String oldPassword;

    @Password(message = USER_PASSWORD_MESSAGE_ERROR)
    private String password;

    @NotBlank(message = CONFIRM_PASSWORD_REQUIRED_MESSAGE_ERROR)
    private String passwordConfirmation;

    public PasswordUserDto() {
        // Empty constructor
    }

    public PasswordUserDto(String username, String oldPassword, String password, String passwordConfirmation) {
        this.username = username;
        this.oldPassword = oldPassword;
        this.password = password;
        this.passwordConfirmation = passwordConfirmation;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
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
}
