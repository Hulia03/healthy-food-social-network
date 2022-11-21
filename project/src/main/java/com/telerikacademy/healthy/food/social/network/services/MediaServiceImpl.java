package com.telerikacademy.healthy.food.social.network.services;

import com.telerikacademy.healthy.food.social.network.exceptions.EntityNotFoundException;
import com.telerikacademy.healthy.food.social.network.models.Media;
import com.telerikacademy.healthy.food.social.network.repositories.contracts.MediaRepository;
import com.telerikacademy.healthy.food.social.network.services.contracts.MediaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

import static com.telerikacademy.healthy.food.social.network.utils.GlobalConstants.*;

@Service
public class MediaServiceImpl implements MediaService {
    private final MediaRepository mediaRepository;

    @Autowired
    public MediaServiceImpl(MediaRepository mediaRepository) {
        this.mediaRepository = mediaRepository;
    }

    @Override
    public Collection<Media> getAll() {
        return mediaRepository.findAll();
    }

    @Override
    public Media getMediaById(long id) {
        return mediaRepository.findById(id)
                .orElseThrow(
                        () -> new EntityNotFoundException(MEDIA, id));
    }

    @Override
    public Media createMedia(Media media) {
        return mediaRepository.save(media);
    }

    @Override
    @Transactional
    public Media updateMedia(Media media) {
        throwExceptionWhenMediaNotExist(media.getId());
        return mediaRepository.save(media);
    }

    @Override
    @Transactional
    public void deleteMedia(long id) {
        throwExceptionWhenMediaNotExist(id);
        mediaRepository.deleteById(id);
    }

    private void throwExceptionWhenMediaNotExist(long id) {
        if (!mediaRepository.existsById(id)) {
            throw new EntityNotFoundException(MEDIA, id);
        }
    }
}
