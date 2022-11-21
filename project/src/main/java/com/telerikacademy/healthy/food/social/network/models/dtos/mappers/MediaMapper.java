package com.telerikacademy.healthy.food.social.network.models.dtos.mappers;

import com.telerikacademy.healthy.food.social.network.models.Media;
import com.telerikacademy.healthy.food.social.network.models.MediaType;
import com.telerikacademy.healthy.food.social.network.services.contracts.FilesService;
import com.telerikacademy.healthy.food.social.network.services.contracts.MediaTypesService;
import com.telerikacademy.healthy.food.social.network.services.contracts.VisibilitiesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class MediaMapper {
    private final MediaTypesService mediaTypesService;
    private final VisibilitiesService visibilitiesService;
    private final FilesService filesService;

    @Autowired
    public MediaMapper(MediaTypesService mediaTypesService, VisibilitiesService visibilitiesService, FilesService filesService) {
        this.mediaTypesService = mediaTypesService;
        this.visibilitiesService = visibilitiesService;
        this.filesService = filesService;
    }

    public Media toMedia(MultipartFile file, int visibilityId, int width) {
        Media media = new Media();
        MediaType type = mediaTypesService.getMediaTypeByType(filesService.getFileType(file));
        media.setVisibility(visibilitiesService.getVisibilityById(visibilityId));
        media.setPicture(filesService.uploadFile(file, width));
        media.setMediaType(type);
        return media;
    }

    public Media mergeMedia(Media oldMedia, MultipartFile file, int visibilityId, int width) {
        oldMedia.setVisibility(visibilitiesService.getVisibilityById(visibilityId));

        if (file == null || file.isEmpty()) {
            return oldMedia;
        }

        MediaType type = mediaTypesService.getMediaTypeByType(filesService.getFileType(file));
        oldMedia.setPicture(filesService.uploadFile(file, width));
        oldMedia.setMediaType(type);
        return oldMedia;
    }
}
