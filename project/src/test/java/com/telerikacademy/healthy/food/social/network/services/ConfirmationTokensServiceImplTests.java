package com.telerikacademy.healthy.food.social.network.services;

import com.telerikacademy.healthy.food.social.network.exceptions.EntityNotFoundException;
import com.telerikacademy.healthy.food.social.network.models.ConfirmationToken;
import com.telerikacademy.healthy.food.social.network.models.User;
import com.telerikacademy.healthy.food.social.network.repositories.contracts.ConfirmationTokensRepository;
import com.telerikacademy.healthy.food.social.network.repositories.contracts.UsersRepository;
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

import static com.telerikacademy.healthy.food.social.network.Factory.createToken;
import static com.telerikacademy.healthy.food.social.network.Factory.createUser;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;

@RunWith(MockitoJUnitRunner.class)
public class ConfirmationTokensServiceImplTests {
    @Mock
    ConfirmationTokensRepository mockRepository;

    @Mock
    UsersRepository mockUserRepository;

    @InjectMocks
    ConfirmationTokensServiceImpl mockService;

    @Test
    public void getAllShould_ReturnListOfConfirmationTokens() {
        // Arrange
        Mockito.when(mockRepository.findAll())
                .thenReturn(Arrays.asList(createToken()));

        // Act
        Collection<ConfirmationToken> list = mockService.getAll();

        // Assert
        Assert.assertEquals(1, list.size());
    }

    @Test
    public void getAllShould_ReturnEmptyListOfConfirmationTokens() {
        // Arrange
        Mockito.when(mockRepository.findAll())
                .thenReturn(Collections.emptyList());

        // Act
        Collection<ConfirmationToken> list = mockService.getAll();

        // Assert
        Assert.assertEquals(0, list.size());
    }

    @Test
    public void getConfirmationTokenByNameShould_ReturnConfirmationToken_WhenConfirmationTokenExists() {
        // Arrange
        ConfirmationToken expected = createToken();

        Mockito.when(mockRepository.findByConfirmationToken(anyString()))
                .thenReturn(Optional.of(expected));

        // Act
        ConfirmationToken actual = mockService.getConfirmationTokenByName(anyString());

        // Assert
        Assert.assertSame(expected, actual);
    }

    @Test
    public void getConfirmationTokenByNameShould_Throw_WhenConfirmationTokenNotExists() {
        // Arrange, Act, Assert
        Assert.assertThrows(EntityNotFoundException.class,
                () -> mockService.getConfirmationTokenByName(anyString()));
    }

    @Test
    public void getConfirmationTokenByNameShould_CallRepository() {
        // Arrange
        ConfirmationToken expected = createToken();

        Mockito.when(mockRepository.findByConfirmationToken(anyString()))
                .thenReturn(Optional.of(expected));

        // Act
        mockService.getConfirmationTokenByName(anyString());

        // Assert
        Mockito.verify(mockRepository,
                times(1)).findByConfirmationToken(anyString());
    }

    @Test
    public void createConfirmationTokenShould_ReturnConfirmationToken() {
        // Arrange
        User user = createUser();
        ConfirmationToken expected = createToken();

        Mockito.when(mockUserRepository.findById(anyString()))
                .thenReturn(Optional.of(user));

        Mockito.when(mockRepository.save(any(ConfirmationToken.class))).thenReturn(expected);

        // Act
        ConfirmationToken actual = mockService.createConfirmationToken(anyString());

        // Assert
        Assert.assertSame(expected, actual);
    }

    @Test
    public void createCategoryShould_Throw_WhenUserNotExist() {
        // Arrange
        Mockito.when(mockUserRepository.findById(anyString()))
                .thenReturn(Optional.empty());

        // Act, Assert
        Assert.assertThrows(EntityNotFoundException.class,
                () -> mockService.createConfirmationToken(anyString()));
    }

}
