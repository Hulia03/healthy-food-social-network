package com.telerikacademy.healthy.food.social.network.services.contracts;

import com.telerikacademy.healthy.food.social.network.models.Media;

import java.util.Collection;

public interface MediaService {
    Collection<Media> getAll();

    Media getMediaById(long id);

    Media createMedia(Media media);

    Media updateMedia(Media media);

    void deleteMedia(long id);
}
