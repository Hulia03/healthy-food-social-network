package com.telerikacademy.healthy.food.social.network.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "posts")
@SQLDelete(sql = "update posts set enabled = false where post_id = ?", check = ResultCheckStyle.COUNT)
@Where(clause = "enabled <> false")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private long id;

    @ManyToOne
    @JoinColumn(name = "visibility_id")
    private Visibility visibility;

    @ManyToOne
    @JoinColumn(name = "creator_id")
    private UserDetails creator;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @OneToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "media_id")
    @NotFound(action = NotFoundAction.IGNORE)
    private Media media;

    @Column(name = "timestamp")
    private Timestamp timestamp;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "posts_categories",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private Set<Category> categories;

    @JsonIgnore
    @OneToMany(mappedBy = "post")
    private Set<Comment> comments;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "posts_likes",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "user_details_id")
    )
    private Set<UserDetails> likedUsers;

    public Post() {
        // Empty constructor
    }

    public Post(long id, Visibility visibility, UserDetails creator, String title, String description, Media media, Collection<UserDetails> likedUsers) {
        this.id = id;
        this.visibility = visibility;
        this.creator = creator;
        this.title = title;
        this.description = description;
        this.media = media;
        this.likedUsers = new HashSet<>(likedUsers);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Visibility getVisibility() {
        return visibility;
    }

    public void setVisibility(Visibility visibility) {
        this.visibility = visibility;
    }

    public UserDetails getCreator() {
        return creator;
    }

    public void setCreator(UserDetails creator) {
        this.creator = creator;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Media getMedia() {
        return media;
    }

    public void setMedia(Media media) {
        this.media = media;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public Set<Category> getCategories() {
        return categories;
    }

    public void setCategories(Set<Category> categories) {
        this.categories = categories;
    }

    public Set<UserDetails> getLikedUsers() {
        return likedUsers;
    }

    public void setLikedUsers(Set<UserDetails> likedUsers) {
        this.likedUsers = likedUsers;
    }

    public Set<Comment> getComments() {
        return comments;
    }

    public void setComments(Set<Comment> comments) {
        this.comments = comments;
    }
}