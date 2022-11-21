package com.telerikacademy.healthy.food.social.network.services;

import com.telerikacademy.healthy.food.social.network.exceptions.EntityNotFoundException;
import com.telerikacademy.healthy.food.social.network.models.UserDetails;
import com.telerikacademy.healthy.food.social.network.repositories.contracts.UserDetailsRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

import static com.telerikacademy.healthy.food.social.network.Factory.createUserDetails;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.times;

@RunWith(MockitoJUnitRunner.class)
public class UserDetailsServicesImplTests {
    @Mock
    Pageable pageable;

    @Mock
    UserDetailsRepository mockRepository;

    @InjectMocks
    UserDetailsServiceImpl mockService;

    @Test
    public void getAllShould_ReturnListOfUserDetailsByFilter() {
        // Arrange
        Page<UserDetails> userDetails = new PageImpl(Arrays.asList(createUserDetails()));
        String filter = "%filter%";
        Mockito.when(mockRepository.findAllByFirstNameLikeAndEnabledTrueOrLastNameLikeAndEnabledTrueOrEmailLikeAndEnabledTrue(filter, filter, filter, pageable))
                .thenReturn(userDetails);

        // Act
        Collection<UserDetails> list = mockService.getAll("filter", pageable);

        // Assert
        Assert.assertEquals(1, list.size());
        Mockito.verify(mockRepository,
                times(0)).findAllByEnabledTrueOrderByFirstNameAsc(pageable);
    }

    @Test
    public void getAllShould_ReturnEmptyListOfUserDetailsByEmptyFilter() {
        // Arrange
        Page<UserDetails> userDetails = new PageImpl(Arrays.asList(createUserDetails()));
        Mockito.when(mockRepository.findAllByEnabledTrueOrderByFirstNameAsc(pageable))
                .thenReturn(userDetails);

        // Act
        Collection<UserDetails> list = mockService.getAll("", pageable);

        // Assert
        Assert.assertEquals(1, list.size());
        Mockito.verify(mockRepository,
                times(0)).findAllByFirstNameLikeAndEnabledTrueOrLastNameLikeAndEnabledTrueOrEmailLikeAndEnabledTrue("", "", "", pageable);
    }

    @Test
    public void getUserDetailsByIdShould_ReturnUserDetails_WhenUserDetailsExists() {
        // Arrange
        UserDetails expected = createUserDetails();

        Mockito.when(mockRepository.findByIdAndEnabledTrue(anyLong()))
                .thenReturn(Optional.of(expected));

        // Act
        UserDetails actual = mockService.getUserDetailsById(anyLong());

        // Assert
        Assert.assertSame(expected, actual);
    }

    @Test
    public void getUserDetailsByIdShould_Throw_WhenUserDetailsNotExists() {
        // Arrange, Act, Assert
        Assert.assertThrows(EntityNotFoundException.class,
                () -> mockService.getUserDetailsById(anyLong()));
    }

    @Test
    public void getUserDetailsByIdShould_CallRepository() {
        // Arrange
        UserDetails expected = createUserDetails();

        Mockito.when(mockRepository.findByIdAndEnabledTrue(anyLong()))
                .thenReturn(Optional.of(expected));

        // Act
        mockService.getUserDetailsById(anyLong());

        // Assert
        Mockito.verify(mockRepository,
                times(1)).findByIdAndEnabledTrue(anyLong());
    }

    @Test
    public void getUserDetailsByEmailShould_ReturnUserDetails_WhenUserDetailsExists() {
        // Arrange
        UserDetails expected = createUserDetails();

        Mockito.when(mockRepository.findByEmailAndEnabledTrue(anyString()))
                .thenReturn(Optional.of(expected));

        // Act
        UserDetails actual = mockService.getUserDetailsByEmail(anyString());

        // Assert
        Assert.assertSame(expected, actual);
    }

    @Test
    public void getUserDetailsByEmailShould_Throw_WhenUserDetailsNotExists() {
        // Arrange, Act, Assert
        Assert.assertThrows(EntityNotFoundException.class,
                () -> mockService.getUserDetailsByEmail(anyString()));
    }

    @Test
    public void getUserDetailsByEmailShould_CallRepository() {
        // Arrange
        UserDetails expected = createUserDetails();

        Mockito.when(mockRepository.findByEmailAndEnabledTrue(anyString()))
                .thenReturn(Optional.of(expected));

        // Act
        mockService.getUserDetailsByEmail(anyString());

        // Assert
        Mockito.verify(mockRepository,
                times(1)).findByEmailAndEnabledTrue(anyString());
    }

    @Test
    public void updateUserDetailsShould_ReturnUserDetails() {
        // Arrange
        UserDetails userDetails = createUserDetails();

        Mockito.when(mockRepository.save(any(UserDetails.class)))
                .thenReturn(userDetails);

        // Act
        UserDetails actual = mockService.updateUserDetails(userDetails);

        // Assert
        Assert.assertSame(userDetails, actual);
    }

    @Test
    public void createUserDetailsShould_ReturnUserDetails() {
        // Arrange
        UserDetails userDetails = createUserDetails();

        Mockito.when(mockRepository.save(any(UserDetails.class)))
                .thenReturn(userDetails);

        // Act
        UserDetails actual = mockService.createUserDetails(userDetails);

        // Assert
        Assert.assertSame(userDetails, actual);
    }

    @Test
    public void adminPermissionShould_ReturnTrue_WhenUserIsAdmin() {
        // Arrange
        Mockito.when(mockRepository.adminPermission(anyLong()))
                .thenReturn(1);

        // Act
        boolean actual = mockService.adminPermission(anyLong());

        // Assert
        Assert.assertTrue(actual);
    }

    @Test
    public void adminPermissionShould_ReturnFalse_WhenUserIsNotAdmin() {
        // Arrange
        Mockito.when(mockRepository.adminPermission(anyLong()))
                .thenReturn(0);

        // Act
        boolean actual = mockService.adminPermission(anyLong());

        // Assert
        Assert.assertFalse(actual);
    }
}
