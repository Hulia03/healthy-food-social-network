package com.telerikacademy.healthy.food.social.network.services;

import com.telerikacademy.healthy.food.social.network.exceptions.EntityNotFoundException;
import com.telerikacademy.healthy.food.social.network.models.Media;
import com.telerikacademy.healthy.food.social.network.models.Post;
import com.telerikacademy.healthy.food.social.network.models.Visibility;
import com.telerikacademy.healthy.food.social.network.repositories.contracts.VisibilitiesRepository;
import com.telerikacademy.healthy.food.social.network.services.contracts.VisibilitiesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

import static com.telerikacademy.healthy.food.social.network.utils.GlobalConstants.*;

@Service
public class VisibilitiesServiceImpl implements VisibilitiesService {
    private final VisibilitiesRepository visibilitiesRepository;

    @Autowired
    public VisibilitiesServiceImpl(VisibilitiesRepository visibilitiesRepository) {
        this.visibilitiesRepository = visibilitiesRepository;
    }

    @Override
    public Collection<Visibility> getAll() {
        return visibilitiesRepository.findAll();
    }

    @Override
    public Visibility getVisibilityById(int id) {
        return visibilitiesRepository.findById(id)
                .orElseThrow(
                        () -> new EntityNotFoundException(VISIBILITY, id)
                );
    }

    @Override
    public Visibility getVisibilityByType(String type) {
        Visibility visibility = visibilitiesRepository.findByType(type);
        if (visibility == null) {
            throw new EntityNotFoundException(VISIBILITY, TYPE, type);
        }

        return visibility;
    }

    @Override
    public boolean checkMediaPublic(Media media) {
        return media.getVisibility().getType().equalsIgnoreCase(PUBLIC);
    }

    @Override
    public boolean checkPostPublic(Post post) {
        return post.getVisibility().getType().equalsIgnoreCase(PUBLIC);
    }
}

