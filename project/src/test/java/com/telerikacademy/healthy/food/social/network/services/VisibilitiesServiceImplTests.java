package com.telerikacademy.healthy.food.social.network.services;

import com.telerikacademy.healthy.food.social.network.exceptions.EntityNotFoundException;
import com.telerikacademy.healthy.food.social.network.models.Media;
import com.telerikacademy.healthy.food.social.network.models.Post;
import com.telerikacademy.healthy.food.social.network.models.Visibility;
import com.telerikacademy.healthy.food.social.network.repositories.contracts.VisibilitiesRepository;
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
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;

@RunWith(MockitoJUnitRunner.class)
public class VisibilitiesServiceImplTests {

    @Mock
    VisibilitiesRepository mockRepository;

    @InjectMocks
    VisibilitiesServiceImpl mockService;

    @Test
    public void getAllShould_ReturnListOfVisibilities() {
        // Arrange
        Mockito.when(mockRepository.findAll())
                .thenReturn(Arrays.asList(createVisibilityPublic()));

        // Act
        Collection<Visibility> list = mockService.getAll();

        // Assert
        Assert.assertEquals(1, list.size());
    }

    @Test
    public void getAllShould_ReturnEmptyListOfVisibilities() {
        // Arrange
        Mockito.when(mockRepository.findAll())
                .thenReturn(Collections.emptyList());

        // Act
        Collection<Visibility> list = mockService.getAll();

        // Assert
        Assert.assertEquals(0, list.size());
    }

    @Test
    public void getVisibilityByIdShould_ReturnVisibility_WhenVisibilityExists() {
        // Arrange
        Visibility expected = createVisibilityPublic();

        Mockito.when(mockRepository.findById(anyInt()))
                .thenReturn(Optional.of(expected));

        // Act
        Visibility actual = mockService.getVisibilityById(anyInt());

        // Assert
        Assert.assertSame(expected, actual);
    }

    @Test
    public void getVisibilityByIdShould_Throw_WhenVisibilityNotExists() {
        // Arrange, Act, Assert
        Assert.assertThrows(EntityNotFoundException.class,
                () -> mockService.getVisibilityById(anyInt()));
    }

    @Test
    public void getVisibilityByIdShould_CallRepository() {
        // Arrange
        Visibility expected = createVisibilityPublic();

        Mockito.when(mockRepository.findById(anyInt()))
                .thenReturn(Optional.of(expected));

        // Act
        mockService.getVisibilityById(anyInt());

        // Assert
        Mockito.verify(mockRepository,
                times(1)).findById(anyInt());
    }

    @Test
    public void getVisibilityByNameShould_ReturnVisibility_WhenVisibilityExists() {
        // Arrange
        Visibility expected = createVisibilityPublic();

        Mockito.when(mockRepository.findByType(anyString()))
                .thenReturn(expected);

        // Act
        Visibility actual = mockService.getVisibilityByType(anyString());

        // Assert
        Assert.assertSame(expected, actual);
    }

    @Test
    public void getVisibilityByTypeShould_Throw_WhenVisibilityNotExists() {
        // Arrange, Act, Assert
        Assert.assertThrows(EntityNotFoundException.class,
                () -> mockService.getVisibilityByType(anyString()));
    }

    @Test
    public void getVisibilityByTypeShould_CallRepository() {
        // Arrange
        Visibility expected = createVisibilityPublic();

        Mockito.when(mockRepository.findByType(anyString()))
                .thenReturn(expected);

        // Act
        mockService.getVisibilityByType(anyString());

        // Assert
        Mockito.verify(mockRepository,
                times(1)).findByType(anyString());
    }

    @Test
    public void checkMediaPublic_ReturnTrue_WhenMediaVisibilityIsPublic() {
        // Arrange
        Media media = createMediaPublic();

        // Act, Assert
        Assert.assertSame(true, mockService.checkMediaPublic(media));
    }

    @Test
    public void checkMediaPublic_ReturnFalse_WhenMediaVisibilityIsConnected() {
        // Arrange
        Media media = createMediaConnected();

        // Act, Assert
        Assert.assertSame(false, mockService.checkMediaPublic(media));
    }

    @Test
    public void checkPostPublic_ReturnTrue_WhenPostVisibilityIsPublic() {
        // Arrange
        Post post = createPostPublic();

        // Act, Assert
        Assert.assertSame(true, mockService.checkPostPublic(post));
    }

    @Test
    public void checkPostPublic_ReturnFalse_WhenPostVisibilityIsConnected() {
        // Arrange
        Post post = createPostConnected();

        // Act, Assert
        Assert.assertSame(false, mockService.checkPostPublic(post));
    }
}
