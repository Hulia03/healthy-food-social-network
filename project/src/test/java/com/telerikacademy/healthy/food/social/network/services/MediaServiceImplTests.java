package com.telerikacademy.healthy.food.social.network.services;

import com.telerikacademy.healthy.food.social.network.exceptions.EntityNotFoundException;
import com.telerikacademy.healthy.food.social.network.models.Media;
import com.telerikacademy.healthy.food.social.network.repositories.contracts.MediaRepository;
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

import static com.telerikacademy.healthy.food.social.network.Factory.createMediaPublic;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.times;

@RunWith(MockitoJUnitRunner.class)
public class MediaServiceImplTests {
    @Mock
    MediaRepository mockRepository;

    @InjectMocks
    MediaServiceImpl mockService;

    @Test
    public void getAllShould_ReturnListOfMedia() {
        // Arrange
        Mockito.when(mockRepository.findAll())
                .thenReturn(Arrays.asList(createMediaPublic()));

        // Act
        Collection<Media> list = mockService.getAll();

        // Assert
        Assert.assertEquals(1, list.size());
    }

    @Test
    public void getAllShould_ReturnEmptyListOfMedia() {
        // Arrange
        Mockito.when(mockRepository.findAll())
                .thenReturn(Collections.emptyList());

        // Act
        Collection<Media> list = mockService.getAll();

        // Assert
        Assert.assertEquals(0, list.size());
    }

    @Test
    public void getMediaByIdShould_ReturnMedia_WhenMediaExists() {
        // Arrange
        Media expected = createMediaPublic();

        Mockito.when(mockRepository.findById(anyLong()))
                .thenReturn(Optional.of(expected));

        // Act
        Media actual = mockService.getMediaById(anyLong());

        // Assert
        Assert.assertSame(expected, actual);
    }

    @Test
    public void getMediaByIdShould_Throw_WhenMediaNotExists() {
        // Arrange, Act, Assert
        Assert.assertThrows(EntityNotFoundException.class,
                () -> mockService.getMediaById(anyLong()));
    }

    @Test
    public void getMediaByIdShould_CallRepository() {
        // Arrange
        Media expected = createMediaPublic();

        Mockito.when(mockRepository.findById(anyLong()))
                .thenReturn(Optional.of(expected));

        // Act
        mockService.getMediaById(anyInt());

        // Assert
        Mockito.verify(mockRepository,
                times(1)).findById(anyLong());
    }

    @Test
    public void createMediaShould_ReturnMedia() {
        // Arrange
        Media media = createMediaPublic();

        Mockito.when(mockRepository.save(any(Media.class))).thenReturn(media);

        // Act
        Media actual = mockService.createMedia(media);

        // Assert
        Assert.assertSame(media, actual);
    }

    @Test
    public void updateMediaShould_ReturnMedia() {
        // Arrange
        Mockito.when(mockRepository.existsById(anyLong()))
                .thenReturn(true);

        Media media = createMediaPublic();
        Mockito.when(mockRepository.save(any(Media.class))).thenReturn(media);

        // Act
        Media actual = mockService.updateMedia(media);

        // Assert
        Assert.assertSame(media, actual);
    }

    @Test
    public void updateMediaShould_Throw_WhenMediaNotExist() {
        // Arrange
        Mockito.when(mockRepository.existsById(anyLong()))
                .thenReturn(false);

        // Act, Assert
        Assert.assertThrows(EntityNotFoundException.class,
                () -> mockService.updateMedia(createMediaPublic()));
    }

    @Test
    public void deleteMediaShould_Throw_WhenMediaNotExist() {
        // Arrange
        Mockito.when(mockRepository.existsById(anyLong()))
                .thenReturn(false);

        // Act, Assert
        Assert.assertThrows(EntityNotFoundException.class,
                () -> mockService.deleteMedia(anyInt()));
    }

    @Test
    public void deleteMediaShould_CallRepository() {
        // Arrange
        Mockito.when(mockRepository.existsById(anyLong()))
                .thenReturn(true);

        // Act
        mockService.deleteMedia(anyInt());

        // Assert
        Mockito.verify(mockRepository, times(1)).deleteById(anyLong());
    }
}