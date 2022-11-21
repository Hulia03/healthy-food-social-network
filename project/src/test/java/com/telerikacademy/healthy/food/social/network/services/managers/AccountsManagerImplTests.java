package com.telerikacademy.healthy.food.social.network.services.managers;

import com.telerikacademy.healthy.food.social.network.exceptions.InvalidRegistrationException;
import com.telerikacademy.healthy.food.social.network.models.ConfirmationToken;
import com.telerikacademy.healthy.food.social.network.models.UserDetails;
import com.telerikacademy.healthy.food.social.network.models.dtos.PasswordUserDto;
import com.telerikacademy.healthy.food.social.network.services.ConfirmationTokensServiceImpl;
import com.telerikacademy.healthy.food.social.network.services.EmailSendersServiceImpl;
import com.telerikacademy.healthy.food.social.network.services.UserDetailsServiceImpl;
import com.telerikacademy.healthy.food.social.network.services.UsersServiceImpl;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.core.env.Environment;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.provisioning.UserDetailsManager;
import org.thymeleaf.context.Context;

import static com.telerikacademy.healthy.food.social.network.Factory.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;

@RunWith(MockitoJUnitRunner.class)
public class AccountsManagerImplTests {
    @Spy
    Environment env;

    @Mock
    UsersServiceImpl mockUsersService;

    @Mock
    UserDetailsServiceImpl mockUserDetailsService;

    @Mock
    EmailSendersServiceImpl mockEmailSendersService;

    @Mock
    ConfirmationTokensServiceImpl mockConfirmationTokensService;

    @Mock
    UserDetailsManager mockUserDetailManager;

    @InjectMocks
    AccountsManagerImpl mockManager;

    @Test
    public void registerShould_CallUserDetailsService() {
        // Arrange
        UserDetails user = createUserDetails();

        // Act
        mockManager.register(user, any(User.class));

        // Assert
        Mockito.verify(mockUserDetailsService,
                times(1)).createUserDetails(user);
    }

    @Test
    public void registerShould_Throw_WhenUserExists() {
        // Arrange
        UserDetails user = createUserDetails();
        Mockito.when(mockUserDetailManager.userExists(anyString()))
                .thenReturn(true);

        // Act, Assert
        Assert.assertThrows(InvalidRegistrationException.class,
                () -> mockManager.register(user, any(User.class)));
    }

    @Test
    public void createConfirmationTokenShould_ReturnConfirmationToken() {
        // Arrange
        ConfirmationToken expected = createToken();
        Mockito.when(mockConfirmationTokensService.createConfirmationToken(anyString()))
                .thenReturn(expected);

        // Act
        ConfirmationToken actual = mockManager.createConfirmationToken(anyString());

        // Assert
        Assert.assertSame(expected, actual);
    }

    @Test
    public void sendConfirmationEmailShould_CallEmailSendersService() {
        // Arrange
        ConfirmationToken token = createToken();

        // Act
        mockManager.sendConfirmationEmail(token, "subject", "path");

        // Assert
        Mockito.verify(mockEmailSendersService,
                times(1)).sendEmail(anyString(), anyString(), anyString(), anyString(), any(Context.class));
    }

    @Test
    public void confirmRegistrationShould_CallUsersService() {
        // Arrange
        ConfirmationToken token = createToken();
        Mockito.when(mockConfirmationTokensService.getConfirmationTokenByName(anyString()))
                .thenReturn(token);

        // Act
        mockManager.confirmRegistration(anyString());

        // Assert
        Mockito.verify(mockUsersService,
                times(1)).confirmUser(token.getUser().getUsername());
    }

    @Test
    public void forgotPasswordShould_ReturnConfirmationToken() {
        // Arrange
        ConfirmationToken expected = createToken();
        Mockito.when(mockUsersService.getUserByName(anyString()))
                .thenReturn(createUser());

        Mockito.when(mockConfirmationTokensService.createConfirmationToken(anyString()))
                .thenReturn(expected);

        // Act
        ConfirmationToken actual = mockManager.forgotPassword(anyString());

        // Assert
        Assert.assertSame(expected, actual);
    }

    @Test
    public void getUsernameByTokenShould_ReturnUsername() {
        // Arrange
        ConfirmationToken token = createToken();
        Mockito.when(mockConfirmationTokensService.getConfirmationTokenByName(anyString()))
                .thenReturn(token);


        // Act
        String actual = mockManager.getUsernameByToken(anyString());

        // Assert
        Assert.assertSame(token.getUser().getUsername(), actual);
    }

    @Test
    public void changePasswordShould_CallUsersService() {
        // Arrange
        com.telerikacademy.healthy.food.social.network.models.User user = createUser();
        PasswordUserDto dto = createPasswordUserDto();
        Mockito.when(mockUsersService.getUserByName(anyString()))
                .thenReturn(user);

        // Act
        mockManager.changePassword(dto);

        // Assert
        Mockito.verify(mockUsersService,
                times(1)).updateUser(user);
    }

    @Test
    public void userExistsShould_ReturnFalse_WhenUserNotExist() {
        // Arrange, Act, Assert
        Assert.assertFalse(mockManager.userExists(anyString()));
    }

    @Test
    public void userExistsShould_ReturnTrue_WhenUserExist() {
        // Arrange
        Mockito.when(mockUserDetailManager.userExists(anyString()))
                .thenReturn(true);
        // Act, Assert
        Assert.assertTrue(mockManager.userExists(anyString()));
    }

}
