package com.telerikacademy.healthy.food.social.network.services;

import com.telerikacademy.healthy.food.social.network.exceptions.DuplicateEntityException;
import com.telerikacademy.healthy.food.social.network.exceptions.EntityNotFoundException;
import com.telerikacademy.healthy.food.social.network.models.Connection;
import com.telerikacademy.healthy.food.social.network.models.Status;
import com.telerikacademy.healthy.food.social.network.models.UserDetails;
import com.telerikacademy.healthy.food.social.network.repositories.contracts.ConnectionsRepository;
import com.telerikacademy.healthy.food.social.network.repositories.contracts.StatusesRepository;
import com.telerikacademy.healthy.food.social.network.repositories.contracts.UserDetailsRepository;
import com.telerikacademy.healthy.food.social.network.services.contracts.ConnectionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static com.telerikacademy.healthy.food.social.network.utils.GlobalConstants.*;

@Service
public class ConnectionsServiceImpl implements ConnectionsService {
    private final ConnectionsRepository connectionsRepository;
    private final StatusesRepository statusesRepository;
    private final UserDetailsRepository userDetailsRepository;

    @Autowired
    public ConnectionsServiceImpl(ConnectionsRepository connectionsRepository,
                                  StatusesRepository statusesRepository,
                                  UserDetailsRepository userDetailsRepository) {
        this.connectionsRepository = connectionsRepository;
        this.statusesRepository = statusesRepository;
        this.userDetailsRepository = userDetailsRepository;
    }

    @Override
    public Collection<UserDetails> getAllConnectedUsers(UserDetails logged, Pageable pageable) {
        Collection<Connection> connected = connectionsRepository.findAllConnections(logged, pageable).getContent();
        Set<UserDetails> connectedUsers = new HashSet<>();
        for (Connection c : connected) {
            if (c.getSender().getId() != logged.getId() && c.getSender().isEnabled()) {
                connectedUsers.add(c.getSender());
            } else if (c.getReceiver().getId() != logged.getId() && c.getReceiver().isEnabled()) {
                connectedUsers.add(c.getReceiver());
            }
        }

        return connectedUsers;
    }

    @Override
    public Collection<UserDetails> getAllRequestedUsers(UserDetails logged, Pageable pageable) {
        return connectionsRepository.findAllRequests(logged, pageable).getContent();
    }

    @Override
    public Connection getConnectionBySenderOrReceiver(UserDetails logged, UserDetails userDetails) {
        Optional<Connection> senderReceiver = connectionsRepository.findBySenderAndReceiver(logged, userDetails);
        if (senderReceiver.isPresent()) {
            return senderReceiver.get();
        }

        Optional<Connection> receiverSender = connectionsRepository.findBySenderAndReceiver(userDetails, logged);
        if (receiverSender.isPresent()) {
            return receiverSender.get();
        }

        return null;
    }

    public Connection getConnectionById(long id) {
        return connectionsRepository.findById(id)
                .orElseThrow(
                        () -> new EntityNotFoundException(CONNECTION, id)
                );
    }

    @Override
    @Transactional
    public UserDetails sendRequest(long receiverId, UserDetails logged) {
        UserDetails receiver = userDetailsRepository.findByIdAndEnabledTrue(receiverId)
                .orElseThrow(
                        () -> new EntityNotFoundException(USER, receiverId)
                );

        if (logged.getId() == receiver.getId()) {
            throw new DuplicateEntityException(YOU_ARE_ALREADY_CONNECTED_WITH_YOURSELF);
        }

        Status connected = statusesRepository.findByType(CONNECTED);
        if (checkUsersConnectionStatus(logged, receiver, connected)) {
            throw new DuplicateEntityException(String.format(ALREADY_CONNECTED_FORMAT_MESSAGE, receiver.getEmail()));
        }

        Status sentRequest = statusesRepository.findByType(SENT_REQUEST);
        if (checkUsersConnectionStatus(logged, receiver, sentRequest)) {
            throw new DuplicateEntityException(REQUEST_IS_ALREADY_SENT);
        }

        Connection connection = new Connection(logged, receiver, sentRequest);
        connectionsRepository.save(connection);
        return receiver;
    }

    @Override
    @Transactional
    public UserDetails confirmRequest(long id, UserDetails logged) {
        Connection connection = getConnectionById(id);
        boolean isAuthorize = userDetailsRepository.adminPermission(logged.getId()) > 0
                || logged.getId() == connection.getReceiver().getId();

        if (!isAuthorize) {
            throw new AccessDeniedException(String.format(CANNOT_MODIFY_CONNECTION_MESSAGE_FORMAT, id));
        }

        Status status = statusesRepository.findByType(CONNECTED);
        connection.setStatus(status);
        return connectionsRepository.save(connection).getSender();
    }

    @Override
    @Transactional
    public void rejectRequest(long id, UserDetails logged) {
        Connection connection = getConnectionById(id);
        boolean isAuthorize = userDetailsRepository.adminPermission(logged.getId()) > 0
                || logged.getId() == connection.getReceiver().getId()
                || logged.getId() == connection.getSender().getId();

        if (!isAuthorize) {
            throw new AccessDeniedException(String.format(CANNOT_MODIFY_CONNECTION_MESSAGE_FORMAT, id));
        }

        connectionsRepository.delete(connection);
    }

    @Override
    public boolean checkUsersConnectionStatus(UserDetails sender, UserDetails receiver, Status status) {
        return connectionsRepository.existsBySenderAndReceiverAndStatus(sender, receiver, status)
                || connectionsRepository.existsBySenderAndReceiverAndStatus(receiver, sender, status);
    }

    @Override
    public boolean checkUsersConnected(UserDetails sender, UserDetails receiver) {
        Status connected = statusesRepository.findByType(CONNECTED);
        return sender.getId() == receiver.getId() || checkUsersConnectionStatus(sender, receiver, connected);
    }
}
