package com.telerikacademy.healthy.food.social.network.services.contracts;

import com.telerikacademy.healthy.food.social.network.models.Connection;
import com.telerikacademy.healthy.food.social.network.models.Status;
import com.telerikacademy.healthy.food.social.network.models.UserDetails;

import java.util.Collection;

import org.springframework.data.domain.Pageable;

public interface ConnectionsService {

    Collection<UserDetails> getAllConnectedUsers(UserDetails logged, Pageable pageable);

    Collection<UserDetails> getAllRequestedUsers(UserDetails logged, Pageable pageable);

    Connection getConnectionBySenderOrReceiver(UserDetails logged, UserDetails userDetails);

    Connection getConnectionById(long id);

    UserDetails sendRequest(long receiverId, UserDetails logged);

    UserDetails confirmRequest(long id, UserDetails logged);

    void rejectRequest(long id, UserDetails logged);

    boolean checkUsersConnectionStatus(UserDetails sender, UserDetails receiver, Status status);

    boolean checkUsersConnected(UserDetails sender, UserDetails receiver);
}
