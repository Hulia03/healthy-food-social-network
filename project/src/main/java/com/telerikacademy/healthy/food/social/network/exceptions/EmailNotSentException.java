package com.telerikacademy.healthy.food.social.network.exceptions;

public class EmailNotSentException extends RuntimeException {
    public EmailNotSentException(String subject, String username) {
        super(String.format("Email with subject: %s cannot be sent to %s", subject, username));
    }
}
