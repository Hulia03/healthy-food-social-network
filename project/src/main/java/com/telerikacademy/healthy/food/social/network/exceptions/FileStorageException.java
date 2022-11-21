package com.telerikacademy.healthy.food.social.network.exceptions;

import static com.telerikacademy.healthy.food.social.network.utils.GlobalConstants.FILE_WAS_NOT_SAVED;

public class FileStorageException extends RuntimeException {
    public FileStorageException(String message) {
        super(String.format(FILE_WAS_NOT_SAVED, message));
    }
}
