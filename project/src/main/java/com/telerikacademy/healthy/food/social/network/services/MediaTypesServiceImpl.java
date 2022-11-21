package com.telerikacademy.healthy.food.social.network.services;

import com.telerikacademy.healthy.food.social.network.exceptions.EntityNotFoundException;
import com.telerikacademy.healthy.food.social.network.models.MediaType;
import com.telerikacademy.healthy.food.social.network.repositories.contracts.MediaTypesRepository;
import com.telerikacademy.healthy.food.social.network.services.contracts.MediaTypesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

import static com.telerikacademy.healthy.food.social.network.utils.GlobalConstants.MEDIA_TYPE;
import static com.telerikacademy.healthy.food.social.network.utils.GlobalConstants.TYPE;

@Service
public class MediaTypesServiceImpl implements MediaTypesService {
    private final MediaTypesRepository mediaTypesRepository;

    @Autowired
    public MediaTypesServiceImpl(MediaTypesRepository mediaTypesRepository) {
        this.mediaTypesRepository = mediaTypesRepository;
    }

    @Override
    public Collection<MediaType> getAll() {
        return mediaTypesRepository.findAll();
    }

    @Override
    public MediaType getMediaTypeById(int id) {
        return mediaTypesRepository.findById(id)
                .orElseThrow(
                        () -> new EntityNotFoundException(MEDIA_TYPE, id)
                );
    }

    @Override
    public MediaType getMediaTypeByType(String type) {
        return mediaTypesRepository.findByType(type)
                .orElseThrow(
                        () -> new EntityNotFoundException(MEDIA_TYPE, TYPE, type)
                );
    }
}
