package com.telerikacademy.healthy.food.social.network.models;

import javax.persistence.*;

@Entity
@Table(name = "connections")
public class Connection {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "connection_id")
    private long id;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    private UserDetails sender;

    @ManyToOne
    @JoinColumn(name = "receiver_id")
    private UserDetails receiver;

    @ManyToOne
    @JoinColumn(name = "status_id")
    private Status status;

    public Connection() {
        // empty constructor
    }

    public Connection(long id, UserDetails sender, UserDetails receiver, Status status) {
        this(sender, receiver, status);
        this.id = id;
    }

    public Connection(UserDetails sender, UserDetails receiver, Status status) {
        this.sender = sender;
        this.receiver = receiver;
        this.status = status;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public UserDetails getSender() {
        return sender;
    }

    public void setSender(UserDetails sender) {
        this.sender = sender;
    }

    public UserDetails getReceiver() {
        return receiver;
    }

    public void setReceiver(UserDetails receiver) {
        this.receiver = receiver;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
