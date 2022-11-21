package com.telerikacademy.healthy.food.social.network.repositories.contracts;


import com.telerikacademy.healthy.food.social.network.models.Connection;
import com.telerikacademy.healthy.food.social.network.models.Status;
import com.telerikacademy.healthy.food.social.network.models.UserDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConnectionsRepository extends JpaRepository<Connection, Long> {
    @Query("select c.sender\n" +
            "from UserDetails ud inner join Connection c on ud.id = c.receiver.id\n" +
            "where c.status.id = 2 and c.receiver = :receiver and c.sender.enabled = true ")
    Page<UserDetails> findAllRequests(UserDetails receiver, Pageable pageable);


    @Query("select c\n" +
            "from UserDetails ud inner join Connection c on ud.id = c.receiver.id\n" +
            "where c.status.id = 1 and ( c.receiver = :user or c.sender = :user)")
    Page<Connection> findAllConnections(UserDetails user, Pageable pageable);

    Optional<Connection> findBySenderAndReceiver(UserDetails sender, UserDetails receiver);

    boolean existsBySenderAndReceiverAndStatus(UserDetails sender, UserDetails receiver, Status status);
}
