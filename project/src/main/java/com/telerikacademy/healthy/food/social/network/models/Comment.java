package com.telerikacademy.healthy.food.social.network.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "comments")
@SQLDelete(sql = "update comments set enabled = false where comment_id = ?", check = ResultCheckStyle.COUNT)
@Where(clause = "enabled <> false")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private long id;

    @ManyToOne
    @JoinColumn(name = "post_id")
    @NotFound(action = NotFoundAction.IGNORE)
    private Post post;

    @OneToOne
    @JoinColumn(name = "creator_id")
    private UserDetails creator;

    @Column(name = "description")
    private String description;

    @Column(name = "timestamp")
    private Timestamp timestamp;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "comments_likes",
            joinColumns = @JoinColumn(name = "comment_id"),
            inverseJoinColumns = @JoinColumn(name = "user_details_id")
    )
    private Set<UserDetails> likedUsers;

    public Comment() {
        // Empty constructor
    }

    public Comment(long id, Post post, UserDetails creator, String description, Timestamp timestamp, Collection<UserDetails> likedUsers) {
        this.id = id;
        this.post = post;
        this.creator = creator;
        this.description = description;
        this.timestamp = timestamp;
        this.likedUsers = new HashSet<>(likedUsers);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public UserDetails getCreator() {
        return creator;
    }

    public void setCreator(UserDetails creator) {
        this.creator = creator;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public Set<UserDetails> getLikedUsers() {
        return likedUsers;
    }

    public void setLikedUsers(Set<UserDetails> likedUsers) {
        this.likedUsers = likedUsers;
    }
}
