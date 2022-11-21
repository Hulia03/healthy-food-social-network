package com.telerikacademy.healthy.food.social.network.services;

import com.telerikacademy.healthy.food.social.network.exceptions.EmailNotSentException;
import com.telerikacademy.healthy.food.social.network.services.contracts.EmailSendersService;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.internet.MimeMessage;


@Service
public class EmailSendersServiceImpl implements EmailSendersService {
    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine emailTemplateEngine;

    public EmailSendersServiceImpl(JavaMailSender javaMailSender, SpringTemplateEngine emailTemplateEngine) {
        this.javaMailSender = javaMailSender;
        this.emailTemplateEngine = emailTemplateEngine;
    }

    @Async
    @Override
    public void sendEmail(String to, String from, String subject, String path, Context context) {
        try {
            String htmlContent = emailTemplateEngine.process("mails/" + path, context);

            MimeMessage message = javaMailSender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(to);
            helper.setFrom(from);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);

            javaMailSender.send(message);
        } catch (Exception ex) {
            throw new EmailNotSentException(subject, to);
        }
    }
}
