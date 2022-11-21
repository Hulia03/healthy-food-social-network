package com.telerikacademy.healthy.food.social.network.services.contracts;

import com.telerikacademy.healthy.food.social.network.models.MediaType;

import java.util.Collection;

public interface MediaTypesService {
    Collection<MediaType> getAll();

    MediaType getMediaTypeById(int id);

    MediaType getMediaTypeByType(String type);
}
