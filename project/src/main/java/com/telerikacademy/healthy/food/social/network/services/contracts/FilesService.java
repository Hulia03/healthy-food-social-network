package com.telerikacademy.healthy.food.social.network.services.contracts;

import org.springframework.web.multipart.MultipartFile;

public interface FilesService {
    String uploadFile(MultipartFile file, int width);

    String getFileType(MultipartFile file);
}
