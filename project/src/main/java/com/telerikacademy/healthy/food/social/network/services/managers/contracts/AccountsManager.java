package com.telerikacademy.healthy.food.social.network.services.managers.contracts;

import com.telerikacademy.healthy.food.social.network.models.ConfirmationToken;
import com.telerikacademy.healthy.food.social.network.models.UserDetails;
import com.telerikacademy.healthy.food.social.network.models.dtos.PasswordUserDto;

public interface AccountsManager {
    void register(UserDetails user, org.springframework.security.core.userdetails.User newUser);

    ConfirmationToken createConfirmationToken(String username);

    void sendConfirmationEmail(ConfirmationToken token, String subject, String path);

    void confirmRegistration(String confirmationToken);

    ConfirmationToken forgotPassword(String username);

    String getUsernameByToken(String confirmationToken);

    void changePassword(PasswordUserDto userDto);

    boolean userExists(String username);
}
