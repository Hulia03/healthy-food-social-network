package com.telerikacademy.healthy.food.social.network.services.managers;

import com.telerikacademy.healthy.food.social.network.exceptions.InvalidRegistrationException;
import com.telerikacademy.healthy.food.social.network.models.ConfirmationToken;
import com.telerikacademy.healthy.food.social.network.models.User;
import com.telerikacademy.healthy.food.social.network.models.UserDetails;
import com.telerikacademy.healthy.food.social.network.models.dtos.PasswordUserDto;
import com.telerikacademy.healthy.food.social.network.services.contracts.ConfirmationTokensService;
import com.telerikacademy.healthy.food.social.network.services.contracts.EmailSendersService;
import com.telerikacademy.healthy.food.social.network.services.contracts.UserDetailsService;
import com.telerikacademy.healthy.food.social.network.services.contracts.UsersService;
import com.telerikacademy.healthy.food.social.network.services.managers.contracts.AccountsManager;
import com.telerikacademy.healthy.food.social.network.utils.GlobalConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.context.Context;

import java.util.Locale;

import static com.telerikacademy.healthy.food.social.network.utils.GlobalConstants.*;

@Component
@PropertySource("classpath:application.properties")
public class AccountsManagerImpl implements AccountsManager {
    private final UsersService usersService;
    private final UserDetailsService userDetailsService;
    private final EmailSendersService emailSendersService;
    private final ConfirmationTokensService confirmationTokensService;
    private final UserDetailsManager userDetailsManager;
    private final String host;
    private final String email;

    @Autowired
    public AccountsManagerImpl(UsersService usersService, UserDetailsService userDetailsService, EmailSendersService emailSendersService, ConfirmationTokensService confirmationTokensService, UserDetailsManager userDetailsManager, Environment env) {
        this.usersService = usersService;
        this.userDetailsService = userDetailsService;
        this.emailSendersService = emailSendersService;
        this.confirmationTokensService = confirmationTokensService;
        this.userDetailsManager = userDetailsManager;

        host = env.getProperty("healthy.food.host");
        email = env.getProperty("spring.mail.username");
    }

    @Override
    @Transactional
    public void register(UserDetails user, org.springframework.security.core.userdetails.User newUser) {
        if (userDetailsManager.userExists(user.getEmail())) {
            throw new InvalidRegistrationException(USER_WITH_THE_SAME_USERNAME_ALREADY_EXISTS);
        }

        userDetailsManager.createUser(newUser);
        userDetailsService.createUserDetails(user);
    }

    @Override
    public ConfirmationToken createConfirmationToken(String username) {
        return confirmationTokensService.createConfirmationToken(username);
    }

    @Override
    public void sendConfirmationEmail(ConfirmationToken token, String subject, String path) {
        Context context = new Context(Locale.ENGLISH);
        context.setVariable(TOKEN, token.getConfirmationToken());
        context.setVariable(HOST_URL, host);

        emailSendersService.sendEmail(token.getUser().getUsername(),
                email,
                subject,
                path,
                context);
    }

    @Override
    @Transactional
    public void confirmRegistration(String confirmationToken) {
        ConfirmationToken token = confirmationTokensService.getConfirmationTokenByName(confirmationToken);
        usersService.confirmUser(token.getUser().getUsername());
    }

    @Override
    @Transactional
    public ConfirmationToken forgotPassword(String username) {
        User user = usersService.getUserByName(username);
        return confirmationTokensService.createConfirmationToken(user.getUsername());
    }

    @Override
    public String getUsernameByToken(String confirmationToken) {
        ConfirmationToken token = confirmationTokensService.getConfirmationTokenByName(confirmationToken);
        return token.getUser().getUsername();
    }

    @Override
    @Transactional
    public void changePassword(PasswordUserDto userDto) {
        User user = usersService.getUserByName(userDto.getUsername());
        user.setPassword(userDto.getPassword());
        usersService.updateUser(user);
    }

    @Override
    public boolean userExists(String username) {
        return userDetailsManager.userExists(username);
    }
}
