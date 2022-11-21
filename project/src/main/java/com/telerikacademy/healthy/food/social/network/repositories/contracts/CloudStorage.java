package com.telerikacademy.healthy.food.social.network.repositories.contracts;

import java.io.IOException;

public interface CloudStorage {
    String uploadFile(byte[] bytes, String fileType, int width) throws IOException;
}
