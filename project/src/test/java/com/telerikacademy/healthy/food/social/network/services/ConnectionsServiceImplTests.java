package com.telerikacademy.healthy.food.social.network.services;

import com.telerikacademy.healthy.food.social.network.exceptions.DuplicateEntityException;
import com.telerikacademy.healthy.food.social.network.exceptions.EntityNotFoundException;
import com.telerikacademy.healthy.food.social.network.models.Connection;
import com.telerikacademy.healthy.food.social.network.models.Status;
import com.telerikacademy.healthy.food.social.network.models.UserDetails;
import com.telerikacademy.healthy.food.social.network.repositories.contracts.ConnectionsRepository;
import com.telerikacademy.healthy.food.social.network.repositories.contracts.StatusesRepository;
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
import org.springframework.security.access.AccessDeniedException;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

import static com.telerikacademy.healthy.food.social.network.Factory.*;
import static com.telerikacademy.healthy.food.social.network.utils.GlobalConstants.CONNECTED;
import static com.telerikacademy.healthy.food.social.network.utils.GlobalConstants.SENT_REQUEST;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;

@RunWith(MockitoJUnitRunner.class)
public class ConnectionsServiceImplTests {
    @Mock
    ConnectionsRepository mockRepository;

    @Mock
    UserDetailsRepository mockUserDetailsRepository;

    @Mock
    StatusesRepository mockStatusesRepository;

    @Mock
    Pageable pageable;

    @InjectMocks
    ConnectionsServiceImpl mockService;

    @Test
    public void getAllConnectedUsersShould_ReturnListOfConnections_WhenLoggedIsSender() {
        // Arrange
        UserDetails logged = createSender();
        Page<Connection> connections = new PageImpl(Arrays.asList(createConnectionSenderReceiver()));
        Mockito.when(mockRepository.findAllConnections(logged, pageable))
                .thenReturn(connections);

        // Act
        Collection<UserDetails> list = mockService.getAllConnectedUsers(logged, pageable);

        // Assert
        Assert.assertEquals(1, list.size());
    }

    @Test
    public void getAllConnectedUsersShould_ReturnListOfConnections_WhenLoggedIsReceiver() {
        // Arrange
        UserDetails logged = createSender();
        Page<Connection> connections = new PageImpl(Arrays.asList(createConnectionReceiverSender()));
        Mockito.when(mockRepository.findAllConnections(logged, pageable))
                .thenReturn(connections);

        // Act
        Collection<UserDetails> list = mockService.getAllConnectedUsers(logged, pageable);

        // Assert
        Assert.assertEquals(1, list.size());
    }

    @Test
    public void getAllConnectedUsersShould_ReturnEmptyListOfConnections() {
        // Arrange
        UserDetails logged = createUserDetails();
        Page<Connection> connections = Mockito.mock(Page.class);
        Mockito.when(mockRepository.findAllConnections(logged, pageable))
                .thenReturn(connections);

        // Act
        Collection<UserDetails> list = mockService.getAllConnectedUsers(logged, pageable);

        // Assert
        Assert.assertEquals(0, list.size());
    }

    @Test
    public void getAllRequestedUsersShould_ReturnListOfUserDetails() {
        // Arrange
        UserDetails logged = createUserDetails();
        Page<UserDetails> users = new PageImpl(Arrays.asList(createUserDetails()));
        Mockito.when(mockRepository.findAllRequests(logged, pageable))
                .thenReturn(users);

        // Act
        Collection<UserDetails> list = mockService.getAllRequestedUsers(logged, pageable);

        // Assert
        Assert.assertEquals(1, list.size());
    }

    @Test
    public void getAllRequestedUsersShould_ReturnEmptyListOfUserDetails() {
        // Arrange
        UserDetails logged = createUserDetails();
        Page<UserDetails> users = new PageImpl(Collections.emptyList());
        Mockito.when(mockRepository.findAllRequests(logged, pageable))
                .thenReturn(users);

        // Act
        Collection<UserDetails> list = mockService.getAllRequestedUsers(logged, pageable);

        // Assert
        Assert.assertEquals(0, list.size());
    }

    @Test
    public void getConnectionBySenderOrReceiverShould_ReturnConnection_WhenLoggedIsSender() {
        // Arrange
        Connection expected = createConnectionSenderReceiver();
        UserDetails logged = createSender();
        UserDetails receiver = createReceiver();
        Mockito.when(mockRepository.findBySenderAndReceiver(logged, receiver))
                .thenReturn(Optional.of(expected));

        // Act
        Connection actual = mockService.getConnectionBySenderOrReceiver(logged, receiver);

        // Assert
        Assert.assertSame(expected, actual);
    }

    @Test
    public void getConnectionBySenderOrReceiverShould_CallRepository_WhenLoggedIsSender() {
        // Arrange
        Connection expected = createConnectionSenderReceiver();
        UserDetails logged = createSender();
        UserDetails receiver = createReceiver();
        Mockito.when(mockRepository.findBySenderAndReceiver(logged, receiver))
                .thenReturn(Optional.of(expected));

        // Act
        mockService.getConnectionBySenderOrReceiver(logged, receiver);

        // Assert
        Mockito.verify(mockRepository,
                times(1)).findBySenderAndReceiver(logged, receiver);
    }

    @Test
    public void getConnectionBySenderOrReceiverShould_ReturnConnection_WhenLoggedIsReceiver() {
        // Arrange
        Connection expected = createConnectionSenderReceiver();
        UserDetails logged = createReceiver();
        UserDetails receiver = createSender();
        Mockito.when(mockRepository.findBySenderAndReceiver(logged, receiver))
                .thenReturn(Optional.empty());

        Mockito.when(mockRepository.findBySenderAndReceiver(receiver, logged))
                .thenReturn(Optional.of(expected));

        // Act
        Connection actual = mockService.getConnectionBySenderOrReceiver(logged, receiver);

        // Assert
        Assert.assertSame(expected, actual);
    }

    @Test
    public void getConnectionBySenderOrReceiverShould_CallRepository_WhenLoggedIsReceiver() {
        // Arrange
        Connection expected = createConnectionSenderReceiver();
        UserDetails logged = createReceiver();
        UserDetails receiver = createSender();
        Mockito.when(mockRepository.findBySenderAndReceiver(logged, receiver))
                .thenReturn(Optional.of(expected));

        // Act
        mockService.getConnectionBySenderOrReceiver(logged, receiver);

        // Assert
        Mockito.verify(mockRepository,
                times(1)).findBySenderAndReceiver(logged, receiver);
    }

    @Test
    public void getConnectionBySenderOrReceiverShould_ReturnNull_WhenConnectionNotExists() {
        // Arrange
        UserDetails logged = createUserDetails();
        UserDetails receiver = createReceiver();
        Mockito.when(mockRepository.findBySenderAndReceiver(logged, receiver))
                .thenReturn(Optional.empty());

        // Act
        Connection actual = mockService.getConnectionBySenderOrReceiver(logged, receiver);

        // Assert
        Assert.assertNull(actual);
    }

    @Test
    public void getConnectionByIdShould_ReturnConnection_WhenConnectionExists() {
        // Arrange
        Connection expected = createConnectionSenderReceiver();

        Mockito.when(mockRepository.findById(anyLong()))
                .thenReturn(Optional.of(expected));

        // Act
        Connection actual = mockService.getConnectionById(anyLong());

        // Assert
        Assert.assertSame(expected, actual);
    }

    @Test
    public void getConnectionByIdShould_Throw_WhenConnectionNotExists() {
        // Arrange, Act, Assert
        Assert.assertThrows(EntityNotFoundException.class,
                () -> mockService.getConnectionById(anyLong()));
    }

    @Test
    public void getConnectionByIdShould_CallRepository() {
        // Arrange
        Connection connection = createConnectionSenderReceiver();

        Mockito.when(mockRepository.findById(anyLong()))
                .thenReturn(Optional.of(connection));

        // Act
        mockService.getConnectionById(anyLong());

        // Assert
        Mockito.verify(mockRepository,
                times(1)).findById(anyLong());
    }

    @Test
    public void sendRequestShould_Throw_WhenReceiverIdNotExists() {
        // Arrange
        UserDetails logged = createUserDetails();

        Mockito.when(mockUserDetailsRepository.findByIdAndEnabledTrue(anyLong()))
                .thenReturn(Optional.empty());

        // Act, Assert
        Assert.assertThrows(EntityNotFoundException.class,
                () -> mockService.sendRequest(anyLong(), logged));
    }

    @Test
    public void sendRequestShould_Throw_WhenSentRequestToYourself() {
        // Arrange
        UserDetails logged = createUserDetails();
        UserDetails receiver = createUserDetails();

        Mockito.when(mockUserDetailsRepository.findByIdAndEnabledTrue(anyLong()))
                .thenReturn(Optional.of(receiver));

        // Act, Assert
        Assert.assertThrows(DuplicateEntityException.class,
                () -> mockService.sendRequest(anyLong(), logged));
    }

    @Test
    public void sendRequestShould_Throw_WhenUsersConnected() {
        // Arrange
        UserDetails logged = createUserDetails();
        UserDetails receiver = createReceiver();

        Mockito.when(mockUserDetailsRepository.findByIdAndEnabledTrue(anyLong()))
                .thenReturn(Optional.of(receiver));

        Status connected = createStatusConnected();
        Mockito.when(mockStatusesRepository.findByType(CONNECTED))
                .thenReturn(connected);

        Mockito.when(mockRepository.existsBySenderAndReceiverAndStatus(logged, receiver, connected))
                .thenReturn(true);

        // Act, Assert
        Assert.assertThrows(DuplicateEntityException.class,
                () -> mockService.sendRequest(anyLong(), logged));
    }

    @Test
    public void sendRequestShould_Throw_WhenRequestIsSent() {
        // Arrange
        UserDetails logged = createUserDetails();
        UserDetails receiver = createReceiver();

        Mockito.when(mockUserDetailsRepository.findByIdAndEnabledTrue(anyLong()))
                .thenReturn(Optional.of(receiver));

        Status connected = createStatusConnected();
        Mockito.when(mockStatusesRepository.findByType(CONNECTED))
                .thenReturn(connected);

        Mockito.when(mockRepository.existsBySenderAndReceiverAndStatus(logged, receiver, connected))
                .thenReturn(false);

        Status sentRequest = createStatusSentRequest();
        Mockito.when(mockStatusesRepository.findByType(SENT_REQUEST))
                .thenReturn(sentRequest);

        Mockito.when(mockRepository.existsBySenderAndReceiverAndStatus(logged, receiver, sentRequest))
                .thenReturn(true);

        // Act, Assert
        Assert.assertThrows(DuplicateEntityException.class,
                () -> mockService.sendRequest(anyLong(), logged));
    }

    @Test
    public void sendRequestShould_ReturnConnection() {
        // Arrange
        UserDetails logged = createUserDetails();
        UserDetails receiver = createReceiver();

        Mockito.when(mockUserDetailsRepository.findByIdAndEnabledTrue(anyLong()))
                .thenReturn(Optional.of(receiver));

        Status status = createStatusConnected();
        Mockito.when(mockStatusesRepository.findByType(anyString()))
                .thenReturn(status);

        Mockito.when(mockRepository.existsBySenderAndReceiverAndStatus(logged, receiver, status))
                .thenReturn(false);

        Mockito.when(mockRepository.existsBySenderAndReceiverAndStatus(receiver, logged, status))
                .thenReturn(false);

        // Act
        UserDetails actual = mockService.sendRequest(anyLong(), logged);

        // Assert
        Assert.assertSame(receiver, actual);
    }

    @Test
    public void confirmRequestShould_Throw_WhenUserIsNotAuthorized() {
        // Arrange
        Connection connection = createConnectionSenderReceiver();
        Mockito.when(mockRepository.findById(anyLong()))
                .thenReturn(Optional.of(connection));

        UserDetails logged = createUserDetails();
        Mockito.when(mockUserDetailsRepository.adminPermission(logged.getId()))
                .thenReturn(0);

        // Act, Assert
        Assert.assertThrows(AccessDeniedException.class,
                () -> mockService.confirmRequest(anyLong(), logged));
    }

    @Test
    public void confirmRequestShould_ReturnSender() {
        // Arrange
        Connection connection = createConnectionSenderReceiver();
        Mockito.when(mockRepository.findById(anyLong()))
                .thenReturn(Optional.of(connection));

        UserDetails logged = createReceiver();
        Mockito.when(mockUserDetailsRepository.adminPermission(logged.getId()))
                .thenReturn(0);

        Status status = createStatusConnected();
        Mockito.when(mockStatusesRepository.findByType(anyString()))
                .thenReturn(status);

        Mockito.when(mockRepository.save(connection))
                .thenReturn(connection);

        // Act
        UserDetails actual = mockService.confirmRequest(anyLong(), logged);

        // Assert
        Assert.assertSame(connection.getSender(), actual);
    }

    @Test
    public void rejectRequestShould_Throw_WhenUserIsNotAuthorized() {
        // Arrange
        Connection connection = createConnectionSenderReceiver();

        Mockito.when(mockRepository.findById(anyLong()))
                .thenReturn(Optional.of(connection));

        UserDetails logged = createUserDetails();
        Mockito.when(mockUserDetailsRepository.adminPermission(logged.getId()))
                .thenReturn(0);

        // Act, Assert
        Assert.assertThrows(AccessDeniedException.class,
                () -> mockService.rejectRequest(anyLong(), logged));
    }

    @Test
    public void rejectRequestShould_CallRepository() {
        // Arrange
        UserDetails logged = createUserDetails();
        Connection connection = createConnectionSenderReceiver();

        Mockito.when(mockRepository.findById(anyLong()))
                .thenReturn(Optional.of(connection));

        Mockito.when(mockUserDetailsRepository.adminPermission(anyLong()))
                .thenReturn(1);

        Mockito.doNothing().when(mockRepository).delete(connection);

        // Act
        mockService.rejectRequest(anyLong(), logged);

        // Assert
        Mockito.verify(mockRepository,
                times(1)).delete(connection);
    }

    @Test
    public void checkUsersConnectedShould_ReturnTrue_WhenUsersConnected() {
        // Arrange
        UserDetails sender = createUserDetails();
        UserDetails receiver = createSender();

        Status status = createStatusConnected();
        Mockito.when(mockStatusesRepository.findByType(anyString()))
                .thenReturn(status);

        Mockito.when(mockRepository.existsBySenderAndReceiverAndStatus(sender, receiver, status))
                .thenReturn(false);

        Mockito.when(mockRepository.existsBySenderAndReceiverAndStatus(receiver, sender, status))
                .thenReturn(true);

        // Act
        boolean actual = mockService.checkUsersConnected(sender, receiver);

        // Assert
        Assert.assertSame(true, actual);
    }
}
