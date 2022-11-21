package com.telerikacademy.healthy.food.social.network.services;


import com.telerikacademy.healthy.food.social.network.exceptions.EntityNotFoundException;
import com.telerikacademy.healthy.food.social.network.models.Gender;
import com.telerikacademy.healthy.food.social.network.repositories.contracts.GenderRepository;
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

import static com.telerikacademy.healthy.food.social.network.Factory.createGender;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.times;

@RunWith(MockitoJUnitRunner.class)
public class GenderServiceImplTests {
    @Mock
    GenderRepository mockRepository;

    @InjectMocks
    GenderServiceImpl mockService;

    @Test
    public void getAllShould_ReturnListOfGender() {
        // Arrange
        Mockito.when(mockRepository.findAll())
                .thenReturn(Arrays.asList(createGender()));

        // Act
        Collection<Gender> list = mockService.getAll();

        // Assert
        Assert.assertEquals(1, list.size());
    }

    @Test
    public void getAllShould_ReturnEmptyListOfGender() {
        // Arrange
        Mockito.when(mockRepository.findAll())
                .thenReturn(Collections.emptyList());

        // Act
        Collection<Gender> list = mockService.getAll();

        // Assert
        Assert.assertEquals(0, list.size());
    }

    @Test
    public void getGenderByIdShould_ReturnGender_WhenGenderExists() {
        // Arrange
        Gender expected = createGender();

        Mockito.when(mockRepository.findById(anyInt()))
                .thenReturn(Optional.of(expected));

        // Act
        Gender actual = mockService.getGenderById(anyInt());

        // Assert
        Assert.assertSame(expected, actual);
    }

    @Test
    public void getGenderByIdShould_Throw_WhenGenderNotExists() {
        // Arrange, Act, Assert
        Assert.assertThrows(EntityNotFoundException.class,
                () -> mockService.getGenderById(anyInt()));
    }

    @Test
    public void getGenderByIdShould_CallRepository() {
        // Arrange
        Gender expected = createGender();

        Mockito.when(mockRepository.findById(anyInt()))
                .thenReturn(Optional.of(expected));

        // Act
        mockService.getGenderById(anyInt());

        // Assert
        Mockito.verify(mockRepository,
                times(1)).findById(anyInt());
    }
}
