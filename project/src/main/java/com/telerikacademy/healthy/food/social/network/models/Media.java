package com.telerikacademy.healthy.food.social.network.models;

import org.hibernate.annotations.ResultCheckStyle;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Entity
@Table(name = "media")
@SQLDelete(sql = "update media set enabled = false where media_id = ?", check = ResultCheckStyle.COUNT)
@Where(clause = "enabled <> false")
public class Media {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "media_id")
    private long id;

    @Column(name = "path")
    private String picture;

    @ManyToOne
    @JoinColumn(name = "type_id")
    private MediaType mediaType;

    @ManyToOne
    @JoinColumn(name = "visibility_id")
    private Visibility visibility;

    public Media() {
        // Empty constructor
    }

    public Media(long id, String picture, MediaType mediaType, Visibility visibility) {
        this.id = id;
        this.picture = picture;
        this.mediaType = mediaType;
        this.visibility = visibility;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public MediaType getMediaType() {
        return mediaType;
    }

    public void setMediaType(MediaType mediaType) {
        this.mediaType = mediaType;
    }

    public Visibility getVisibility() {
        return visibility;
    }

    public void setVisibility(Visibility visibility) {
        this.visibility = visibility;
    }
}
