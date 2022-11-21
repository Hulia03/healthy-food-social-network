package com.telerikacademy.healthy.food.social.network.services.contracts;

import org.thymeleaf.context.Context;

public interface EmailSendersService {
    void sendEmail(String to, String from, String subject, String path, Context context);
}
