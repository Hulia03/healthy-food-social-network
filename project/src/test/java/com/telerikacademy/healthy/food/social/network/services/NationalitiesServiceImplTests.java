package com.telerikacademy.healthy.food.social.network.services;

import com.telerikacademy.healthy.food.social.network.exceptions.DuplicateEntityException;
import com.telerikacademy.healthy.food.social.network.exceptions.EntityNotFoundException;
import com.telerikacademy.healthy.food.social.network.models.Nationality;
import com.telerikacademy.healthy.food.social.network.repositories.contracts.NationalitiesRepository;
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

import static com.telerikacademy.healthy.food.social.network.Factory.createNationalityBG;
import static com.telerikacademy.healthy.food.social.network.Factory.createNationalityEN;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.times;

@RunWith(MockitoJUnitRunner.class)
public class NationalitiesServiceImplTests {
    @Mock
    NationalitiesRepository mockRepository;

    @InjectMocks
    NationalitiesServiceImpl mockService;

    @Test
    public void getAllShould_ReturnListOfNationalities() {
        // Arrange
        Mockito.when(mockRepository.findAll())
                .thenReturn(Arrays.asList(createNationalityBG()));

        // Act
        Collection<Nationality> list = mockService.getAll();

        // Assert
        Assert.assertEquals(1, list.size());
    }

    @Test
    public void getAllShould_ReturnEmptyListOfNationalities() {
        // Arrange
        Mockito.when(mockRepository.findAll())
                .thenReturn(Collections.emptyList());

        // Act
        Collection<Nationality> list = mockService.getAll();

        // Assert
        Assert.assertEquals(0, list.size());
    }

    @Test
    public void getNationalityByIdShould_ReturnNationality_WhenNationalityExists() {
        // Arrange
        Nationality expected = createNationalityBG();

        Mockito.when(mockRepository.findById(anyInt()))
                .thenReturn(Optional.of(expected));

        // Act
        Nationality actual = mockService.getNationalityById(anyInt());

        // Assert
        Assert.assertSame(expected, actual);
    }

    @Test
    public void getNationalityByIdShould_Throw_WhenNationalityNotExists() {
        // Arrange, Act, Assert
        Assert.assertThrows(EntityNotFoundException.class,
                () -> mockService.getNationalityById(anyInt()));
    }

    @Test
    public void getNationalityByIdShould_CallRepository() {
        // Arrange
        Nationality expected = createNationalityBG();

        Mockito.when(mockRepository.findById(anyInt()))
                .thenReturn(Optional.of(expected));

        // Act
        mockService.getNationalityById(anyInt());

        // Assert
        Mockito.verify(mockRepository,
                times(1)).findById(anyInt());
    }

    @Test
    public void createNationalityShould_ReturnNationality() {
        // Arrange
        Nationality nationality = createNationalityBG();

        Mockito.when(mockRepository.save(any(Nationality.class))).thenReturn(nationality);

        // Act
        Nationality actual = mockService.createNationality(nationality);

        // Assert
        Assert.assertSame(nationality, actual);
    }

    @Test
    public void createNationalityShould_Throw_WhenNationalityNameExist() {
        // Arrange
        Nationality nationality = createNationalityBG();
        Mockito.when(mockRepository.findByNationality(anyString()))
                .thenReturn(Optional.of(nationality));

        // Act, Assert
        Assert.assertThrows(DuplicateEntityException.class,
                () -> mockService.createNationality(nationality));
    }

    @Test
    public void updateNationalityShould_ReturnNationality() {
        // Arrange
        Mockito.when(mockRepository.existsById(anyInt()))
                .thenReturn(true);

        Nationality nationality = createNationalityBG();
        Mockito.when(mockRepository.save(any(Nationality.class))).thenReturn(nationality);

        // Act
        Nationality actual = mockService.updateNationality(nationality);

        // Assert
        Assert.assertSame(nationality, actual);
    }

    @Test
    public void updateNationalityShould_Throw_WhenNationalityNotExist() {
        // Arrange
        Mockito.when(mockRepository.existsById(anyInt()))
                .thenReturn(false);

        // Act, Assert
        Assert.assertThrows(EntityNotFoundException.class,
                () -> mockService.updateNationality(createNationalityBG()));
    }

    @Test
    public void updateNationalityShould_Throw_WhenNationalityNameExist() {
        // Arrange
        Nationality nationality = createNationalityBG();
        Nationality update = createNationalityEN();
        Mockito.when(mockRepository.existsById(anyInt()))
                .thenReturn(true);
        Mockito.when(mockRepository.existsNationalitiesByIdAndNationality(anyInt(), anyString()))
                .thenReturn(false);

        Mockito.when(mockRepository.findByNationality(anyString()))
                .thenReturn(Optional.of(nationality));

        // Act, Assert
        Assert.assertThrows(DuplicateEntityException.class,
                () -> mockService.updateNationality(createNationalityBG()));
    }

    @Test
    public void deleteNationalityShould_Throw_WhenNationalityNotExist() {
        // Arrange
        Mockito.when(mockRepository.existsById(anyInt()))
                .thenReturn(false);

        // Act, Assert
        Assert.assertThrows(EntityNotFoundException.class,
                () -> mockService.deleteNationality(anyInt()));
    }

    @Test
    public void deleteNationalityShould_CallRepository() {
        // Arrange
        Mockito.when(mockRepository.existsById(anyInt()))
                .thenReturn(true);

        // Act
        mockService.deleteNationality(anyInt());

        // Assert
        Mockito.verify(mockRepository, times(1)).deleteById(anyInt());
    }
}
