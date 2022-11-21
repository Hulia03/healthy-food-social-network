package com.telerikacademy.healthy.food.social.network.services;


import com.telerikacademy.healthy.food.social.network.exceptions.EntityNotFoundException;
import com.telerikacademy.healthy.food.social.network.models.MediaType;
import com.telerikacademy.healthy.food.social.network.repositories.contracts.MediaTypesRepository;
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

import static com.telerikacademy.healthy.food.social.network.Factory.createMediaType;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;

@RunWith(MockitoJUnitRunner.class)
public class MediaTypesServiceImplTests {
    @Mock
    MediaTypesRepository mockRepository;

    @InjectMocks
    MediaTypesServiceImpl mockService;

    @Test
    public void getAllShould_ReturnListOfMediaTypes() {
        // Arrange
        Mockito.when(mockRepository.findAll())
                .thenReturn(Arrays.asList(createMediaType()));

        // Act
        Collection<MediaType> list = mockService.getAll();

        // Assert
        Assert.assertEquals(1, list.size());
    }

    @Test
    public void getAllShould_ReturnEmptyListOfMediaTypes() {
        // Arrange
        Mockito.when(mockRepository.findAll())
                .thenReturn(Collections.emptyList());

        // Act
        Collection<MediaType> list = mockService.getAll();

        // Assert
        Assert.assertEquals(0, list.size());
    }

    @Test
    public void getMediaTypeByIdShould_ReturnMediaType_WhenMediaTypeExists() {
        // Arrange
        MediaType expected = createMediaType();

        Mockito.when(mockRepository.findById(anyInt()))
                .thenReturn(Optional.of(expected));

        // Act
        MediaType actual = mockService.getMediaTypeById(anyInt());

        // Assert
        Assert.assertSame(expected, actual);
    }

    @Test
    public void getMediaTypeByIdShould_Throw_WhenMediaTypeNotExists() {
        // Arrange, Act, Assert
        Assert.assertThrows(EntityNotFoundException.class,
                () -> mockService.getMediaTypeById(anyInt()));
    }

    @Test
    public void getMediaTypeByIdShould_CallRepository() {
        // Arrange
        MediaType expected = createMediaType();

        Mockito.when(mockRepository.findById(anyInt()))
                .thenReturn(Optional.of(expected));

        // Act
        mockService.getMediaTypeById(anyInt());

        // Assert
        Mockito.verify(mockRepository,
                times(1)).findById(anyInt());
    }

    @Test
    public void getMediaTypeByNameShould_ReturnMediaType_WhenMediaTypeExists() {
        // Arrange
        MediaType expected = createMediaType();

        Mockito.when(mockRepository.findByType(anyString()))
                .thenReturn(Optional.of(expected));

        // Act
        MediaType actual = mockService.getMediaTypeByType(anyString());

        // Assert
        Assert.assertSame(expected, actual);
    }

    @Test
    public void getMediaTypeByTypeShould_Throw_WhenMediaTypeNotExists() {
        // Arrange, Act, Assert
        Assert.assertThrows(EntityNotFoundException.class,
                () -> mockService.getMediaTypeByType(anyString()));
    }

    @Test
    public void getMediaTypeByTypeShould_CallRepository() {
        // Arrange
        MediaType expected = createMediaType();

        Mockito.when(mockRepository.findByType(anyString()))
                .thenReturn(Optional.of(expected));

        // Act
        mockService.getMediaTypeByType(anyString());

        // Assert
        Mockito.verify(mockRepository,
                times(1)).findByType(anyString());
    }
}

