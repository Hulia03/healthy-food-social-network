package com.telerikacademy.healthy.food.social.network.services;

import com.telerikacademy.healthy.food.social.network.exceptions.EntityNotFoundException;
import com.telerikacademy.healthy.food.social.network.models.Status;
import com.telerikacademy.healthy.food.social.network.repositories.contracts.StatusesRepository;
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

import static com.telerikacademy.healthy.food.social.network.Factory.createStatusConnected;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;

@RunWith(MockitoJUnitRunner.class)
public class StatusesServiceImplTests {
    @Mock
    StatusesRepository mockRepository;

    @InjectMocks
    StatusesServiceImpl mockService;

    @Test
    public void getAllShould_ReturnListOfStatuses() {
        // Arrange
        Mockito.when(mockRepository.findAll())
                .thenReturn(Arrays.asList(createStatusConnected()));

        // Act
        Collection<Status> list = mockService.getAll();

        // Assert
        Assert.assertEquals(1, list.size());
    }

    @Test
    public void getAllShould_ReturnEmptyListOfStatuses() {
        // Arrange
        Mockito.when(mockRepository.findAll())
                .thenReturn(Collections.emptyList());

        // Act
        Collection<Status> list = mockService.getAll();

        // Assert
        Assert.assertEquals(0, list.size());
    }

    @Test
    public void getStatusByIdShould_ReturnStatus_WhenStatusExists() {
        // Arrange
        Status expected = createStatusConnected();

        Mockito.when(mockRepository.findById(anyInt()))
                .thenReturn(Optional.of(expected));

        // Act
        Status actual = mockService.getStatusById(anyInt());

        // Assert
        Assert.assertSame(expected, actual);
    }

    @Test
    public void getStatusByIdShould_Throw_WhenStatusNotExists() {
        // Arrange, Act, Assert
        Assert.assertThrows(EntityNotFoundException.class,
                () -> mockService.getStatusById(anyInt()));
    }

    @Test
    public void getStatusByIdShould_CallRepository() {
        // Arrange
        Status expected = createStatusConnected();

        Mockito.when(mockRepository.findById(anyInt()))
                .thenReturn(Optional.of(expected));

        // Act
        mockService.getStatusById(anyInt());

        // Assert
        Mockito.verify(mockRepository,
                times(1)).findById(anyInt());
    }

    @Test
    public void getStatusByNameShould_ReturnStatus_WhenStatusExists() {
        // Arrange
        Status expected = createStatusConnected();

        Mockito.when(mockRepository.findByType(anyString()))
                .thenReturn(expected);

        // Act
        Status actual = mockService.getStatusByType(anyString());

        // Assert
        Assert.assertSame(expected, actual);
    }

    @Test
    public void getStatusByTypeShould_Throw_WhenStatusNotExists() {
        // Arrange, Act, Assert
        Assert.assertThrows(EntityNotFoundException.class,
                () -> mockService.getStatusByType(anyString()));
    }

    @Test
    public void getStatusByTypeShould_CallRepository() {
        // Arrange
        Status expected = createStatusConnected();

        Mockito.when(mockRepository.findByType(anyString()))
                .thenReturn(expected);

        // Act
        mockService.getStatusByType(anyString());

        // Assert
        Mockito.verify(mockRepository,
                times(1)).findByType(anyString());
    }
}


