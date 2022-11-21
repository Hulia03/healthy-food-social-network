package com.telerikacademy.healthy.food.social.network.services;

import com.telerikacademy.healthy.food.social.network.exceptions.EntityNotFoundException;
import com.telerikacademy.healthy.food.social.network.models.User;
import com.telerikacademy.healthy.food.social.network.models.UserDetails;
import com.telerikacademy.healthy.food.social.network.repositories.contracts.UserDetailsRepository;
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

import static com.telerikacademy.healthy.food.social.network.Factory.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.times;

@RunWith(MockitoJUnitRunner.class)
public class UsersServiceImplTests {
    @Mock
    UsersRepository mockUsersRepository;

    @Mock
    UserDetailsRepository mockUserDetailsRepository;

    @InjectMocks
    UsersServiceImpl mockService;

    @Test
    public void getAllShould_ReturnListOfUsers() {
        // Arrange
        Mockito.when(mockUsersRepository.findAll())
                .thenReturn(Arrays.asList(createUser()));

        // Act
        Collection<User> list = mockService.getAll();

        // Assert
        Assert.assertEquals(1, list.size());
    }

    @Test
    public void getAllShould_ReturnEmptyListOfUsers() {
        // Arrange
        Mockito.when(mockUsersRepository.findAll())
                .thenReturn(Collections.emptyList());

        // Act
        Collection<User> list = mockService.getAll();

        // Assert
        Assert.assertEquals(0, list.size());
    }

    @Test
    public void getUserByNameShould_ReturnUser_WhenUserExists() {
        // Arrange
        User expected = createUser();

        Mockito.when(mockUsersRepository.findById(anyString()))
                .thenReturn(Optional.of(expected));

        // Act
        User actual = mockService.getUserByName(anyString());

        // Assert
        Assert.assertSame(expected, actual);
    }

    @Test
    public void getUserByNameShould_Throw_WhenUserNotExists() {
        // Arrange, Act, Assert
        Assert.assertThrows(EntityNotFoundException.class,
                () -> mockService.getUserByName(anyString()));
    }

    @Test
    public void getUserByNameShould_CallRepository() {
        // Arrange
        User expected = createUser();

        Mockito.when(mockUsersRepository.findById(anyString()))
                .thenReturn(Optional.of(expected));

        // Act
        mockService.getUserByName(anyString());

        // Assert
        Mockito.verify(mockUsersRepository,
                times(1)).findById(anyString());
    }

    @Test
    public void updateUserShould_ReturnUser() {
        // Arrange
        Mockito.when(mockUsersRepository.existsById(anyString()))
                .thenReturn(true);

        User user = createUser();
        Mockito.when(mockUsersRepository.save(any(User.class))).thenReturn(user);

        // Act
        User actual = mockService.updateUser(user);

        // Assert
        Assert.assertSame(user, actual);
    }

    @Test
    public void updateUserShould_Throw_WhenUserNotExist() {
        // Arrange
        Mockito.when(mockUsersRepository.existsById(anyString()))
                .thenReturn(false);

        // Act, Assert
        Assert.assertThrows(EntityNotFoundException.class,
                () -> mockService.updateUser(createUser()));
    }

    @Test
    public void deleteUserShould_Throw_WhenUserNotExist() {
        // Arrange, Act, Assert
        Assert.assertThrows(EntityNotFoundException.class,
                () -> mockService.deleteUser(anyString()));
    }

    @Test
    public void deleteUserShould_Throw_WhenUserDetailsNotExist() {
        // Arrange
        Mockito.when(mockUsersRepository.findById(anyString()))
                .thenReturn(Optional.of(createUser()));

        // Act, Assert
        Assert.assertThrows(EntityNotFoundException.class,
                () -> mockService.deleteUser(anyString()));
    }

    @Test
    public void deleteUserShould_CallRepository() {
        // Arrange
        User user = createUser();

        UserDetails userDetails = createUserDetails();
        Mockito.when(mockUsersRepository.findById(anyString()))
                .thenReturn(Optional.of(user));

        Mockito.when(mockUserDetailsRepository.findByEmailAndEnabledTrue(anyString()))
                .thenReturn(Optional.of(userDetails));

        // Act
        mockService.deleteUser(anyString());

        // Assert
        Mockito.verify(mockUsersRepository, times(1)).save(user);
        Mockito.verify(mockUserDetailsRepository, times(1)).save(userDetails);
    }

    @Test
    public void confirmUserShould_Throw_WhenUserNotExist() {
        // Arrange, Act, Assert
        Assert.assertThrows(EntityNotFoundException.class,
                () -> mockService.confirmUser(anyString()));
    }

    @Test
    public void confirmUserShould_Throw_WhenUserDetailsNotExist() {
        // Arrange
        Mockito.when(mockUsersRepository.findById(anyString()))
                .thenReturn(Optional.of(createUser()));

        // Act, Assert
        Assert.assertThrows(EntityNotFoundException.class,
                () -> mockService.confirmUser(anyString()));
    }

    @Test
    public void confirmUserShould_CallRepository() {
        // Arrange
        User user = createUser();

        Mockito.when(mockUsersRepository.findById(anyString()))
                .thenReturn(Optional.of(user));

        UserDetails userDetails = createUserDetails();
        Mockito.when(mockUserDetailsRepository.findByEmail(anyString()))
                .thenReturn(Optional.of(userDetails));

        // Act
        mockService.confirmUser(anyString());

        // Assert
        Mockito.verify(mockUsersRepository, times(1)).save(user);
    }
}
