package com.telerikacademy.healthy.food.social.network.services.contracts;

import com.telerikacademy.healthy.food.social.network.models.Media;
import com.telerikacademy.healthy.food.social.network.models.Post;
import com.telerikacademy.healthy.food.social.network.models.Visibility;

import java.util.Collection;

public interface VisibilitiesService {
    Collection<Visibility> getAll();

    Visibility getVisibilityById(int id);

    Visibility getVisibilityByType(String type);

    boolean checkMediaPublic(Media media);

    boolean checkPostPublic(Post post);
}
