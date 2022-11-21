package com.telerikacademy.healthy.food.social.network.services.managers;

import com.telerikacademy.healthy.food.social.network.models.Media;
import com.telerikacademy.healthy.food.social.network.models.UserDetails;
import com.telerikacademy.healthy.food.social.network.models.dtos.mappers.UserDetailsMapper;
import com.telerikacademy.healthy.food.social.network.models.dtos.users.UserDetailsDto;
import com.telerikacademy.healthy.food.social.network.services.ConnectionsServiceImpl;
import com.telerikacademy.healthy.food.social.network.services.UserDetailsServiceImpl;
import com.telerikacademy.healthy.food.social.network.services.UsersServiceImpl;
import com.telerikacademy.healthy.food.social.network.services.VisibilitiesServiceImpl;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.access.AccessDeniedException;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import static com.telerikacademy.healthy.food.social.network.Factory.createSender;
import static com.telerikacademy.healthy.food.social.network.Factory.createUserDetails;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;


@RunWith(MockitoJUnitRunner.class)
public class UsersManagerImplTests {
    @Mock
    UsersServiceImpl mockUsersService;

    @Mock
    UserDetailsServiceImpl mockUserDetailsService;

    @Mock
    ConnectionsServiceImpl mockConnectionsService;

    @Mock
    VisibilitiesServiceImpl mockVisibilitiesService;

    @Spy
    UserDetailsMapper spyUserDetailsMapper;

    @InjectMocks
    UsersManagerImpl mockManager;

    @Test
    public void getAllShould_ReturnListOfUserDetailsDto() {
        // Arrange

        // Act
        Collection<UserDetailsDto> list = mockManager.getAll(Arrays.asList(createUserDetails()));

        // Assert
        Assert.assertEquals(1, list.size());
    }

    @Test
    public void getAllShould_ReturnEmptyListOfUserDetailsDto() {
        // Arrange


        // Act
        Collection<UserDetailsDto> list = mockManager.getAll(Collections.emptyList());

        // Assert
        Assert.assertEquals(0, list.size());
    }

    @Test
    public void getAllShould_ReturnListOfUserDetailsDto_WhenUserIsLogged() {
        // Arrange

        // Act
        Collection<UserDetailsDto> list = mockManager.getAll(Arrays.asList(createUserDetails()), createSender());

        // Assert
        Assert.assertEquals(1, list.size());
    }

    @Test
    public void getAllShould_ReturnEmptyListOfUserDetailsDto_WhenUserIsLogged() {
        // Arrange, Act
        Collection<UserDetailsDto> list = mockManager.getAll(Collections.emptyList(), createSender());

        // Assert
        Assert.assertEquals(0, list.size());
    }


    @Test
    public void getUserDetailsByIdPublicShould_ReturnUserDetailsDtoWithPicture_WhenPictureIsPublic() {
        // Arrange
        UserDetails user = createUserDetails();
        Mockito.when(mockUserDetailsService.getUserDetailsById(anyLong()))
                .thenReturn(user);

        Mockito.when(mockVisibilitiesService.checkMediaPublic(any(Media.class)))
                .thenReturn(true);

        // Act
        UserDetailsDto actual = mockManager.getUserDetailsByIdPublic(user.getId());

        // Assert
        Assert.assertNotNull(actual.getPicture());
    }

    @Test
    public void getUserDetailsByIdPublicShould_ReturnUserDetailsDtoWithoutPicture_WhenPictureIsForConnected() {
        // Arrange
        UserDetails user = createUserDetails();
        Mockito.when(mockUserDetailsService.getUserDetailsById(anyLong()))
                .thenReturn(user);

        Mockito.when(mockVisibilitiesService.checkMediaPublic(any(Media.class)))
                .thenReturn(false);

        // Act
        UserDetailsDto actual = mockManager.getUserDetailsByIdPublic(user.getId());

        // Assert
        Assert.assertNull(actual.getPicture());
    }

    @Test
    public void getUserDetailsByIdShould_ReturnUserDetailsDtoWithPicture_WhenUsersAreConnected() {
        // Arrange
        UserDetails user = createUserDetails();
        Mockito.when(mockUserDetailsService.getUserDetailsById(anyLong()))
                .thenReturn(user);

        Mockito.when(mockConnectionsService.checkUsersConnected(any(UserDetails.class), any(UserDetails.class)))
                .thenReturn(true);

        // Act
        UserDetailsDto actual = mockManager.getUserDetailsById(user.getId(), createSender());

        // Assert
        Assert.assertNotNull(actual.getPicture());
    }

    @Test
    public void getUserDetailsByIdShould_ReturnUserDetailsDtoWithoutPicture_WhenUsersAreNotConnected() {
        // Arrange
        UserDetails user = createUserDetails();
        Mockito.when(mockUserDetailsService.getUserDetailsById(anyLong()))
                .thenReturn(user);

        Mockito.when(mockVisibilitiesService.checkMediaPublic(any(Media.class)))
                .thenReturn(false);

        Mockito.when(mockUserDetailsService.adminPermission(anyLong()))
                .thenReturn(false);

        Mockito.when(mockConnectionsService.checkUsersConnected(any(UserDetails.class), any(UserDetails.class)))
                .thenReturn(false);

        // Act
        UserDetailsDto actual = mockManager.getUserDetailsById(user.getId(), createSender());

        // Assert
        Assert.assertNull(actual.getPicture());
    }

    @Test
    public void getUserDetailsByIdShould_ReturnLUserDetailsDtoWithPicture_WhenUserIsAdmin() {
        // Arrange
        UserDetails user = createUserDetails();
        Mockito.when(mockUserDetailsService.getUserDetailsById(anyLong()))
                .thenReturn(user);

        Mockito.when(mockVisibilitiesService.checkMediaPublic(any(Media.class)))
                .thenReturn(false);

        Mockito.when(mockUserDetailsService.adminPermission(anyLong()))
                .thenReturn(true);

        Mockito.when(mockConnectionsService.checkUsersConnected(any(UserDetails.class), any(UserDetails.class)))
                .thenReturn(false);
        // Act
        UserDetailsDto actual = mockManager.getUserDetailsById(user.getId(), createSender());

        // Assert
        Assert.assertNotNull(actual.getPicture());
    }

    @Test
    public void getUserDetailsByIdShould_ReturnUserDetailsDtoWithoutPicture_WhenUserIsNotAdmin() {
        // Arrange
        UserDetails user = createUserDetails();
        Mockito.when(mockUserDetailsService.getUserDetailsById(anyLong()))
                .thenReturn(user);

        Mockito.when(mockVisibilitiesService.checkMediaPublic(any(Media.class)))
                .thenReturn(false);

        Mockito.when(mockUserDetailsService.adminPermission(anyLong()))
                .thenReturn(false);

        Mockito.when(mockConnectionsService.checkUsersConnected(any(UserDetails.class), any(UserDetails.class)))
                .thenReturn(false);

        // Act
        UserDetailsDto actual = mockManager.getUserDetailsById(user.getId(), createSender());

        // Assert
        Assert.assertNull(actual.getPicture());
    }

    @Test
    public void getUserDetailsByIdShould_ReturnUserDetailsDtoWithPicture_WhenUserIsTheUser() {
        // Arrange
        UserDetails user = createUserDetails();
        Mockito.when(mockUserDetailsService.getUserDetailsById(anyLong()))
                .thenReturn(user);

        // Act
        UserDetailsDto actual = mockManager.getUserDetailsById(user.getId(), user);

        // Assert
        Assert.assertNotNull(actual.getPicture());
    }

    @Test
    public void updateUserDetailsShould_ReturnUserDetails() {
        // Arrange
        UserDetails user = createUserDetails();
        Mockito.when(mockUserDetailsService.updateUserDetails(any(UserDetails.class)))
                .thenReturn(user);

        // Act
        UserDetails actual = mockManager.updateUserDetails(user, user);

        // Assert
        Assert.assertSame(user, actual);
    }

    @Test
    public void updateUserDetailsShould_Throw_WhenUserIsNotAuthorize() {
        // Arrange
        UserDetails logged = createSender();
        UserDetails user = createUserDetails();

        Mockito.when(mockUserDetailsService.adminPermission(anyLong()))
                .thenReturn(false);

        // Act, Assert
        Assert.assertThrows(AccessDeniedException.class,
                () -> mockManager.updateUserDetails(user, logged));
    }

    @Test
    public void deleteUserShould_CallUserService() {
        // Arrange
        UserDetails user = createUserDetails();
        Mockito.when(mockUserDetailsService.getUserDetailsById(anyLong()))
                .thenReturn(user);

        // Act
        mockManager.deleteUser(user.getId());

        // Assert
        Mockito.verify(mockUsersService, times(1)).deleteUser(user.getEmail());
    }
}
