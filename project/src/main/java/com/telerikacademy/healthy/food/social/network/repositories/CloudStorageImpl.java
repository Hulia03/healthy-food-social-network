package com.telerikacademy.healthy.food.social.network.repositories;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.utils.ObjectUtils;
import com.telerikacademy.healthy.food.social.network.repositories.contracts.CloudStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.Map;

@Repository
public class CloudStorageImpl implements CloudStorage {
    private final Cloudinary cloudinary;

    @Autowired
    public CloudStorageImpl(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    @Override
    public String uploadFile(byte[] bytes, String fileType, int width) throws IOException {
        Map uploadResult = cloudinary
                .uploader()
                .upload(bytes,
                        ObjectUtils.asMap(
                                "transformation",
                                new Transformation()
                                        .crop("limit")
                                        .width(width),
                                "resource_type", fileType));
        return uploadResult.get("url").toString();
    }
}
