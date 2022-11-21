package com.telerikacademy.healthy.food.social.network.services;

import com.telerikacademy.healthy.food.social.network.exceptions.EmailNotSentException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.mail.javamail.JavaMailSender;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.springframework.core.env.Environment;

import javax.mail.internet.MimeMessage;

import static com.telerikacademy.healthy.food.social.network.Factory.createContext;
import static com.telerikacademy.healthy.food.social.network.Factory.createMimeMessage;

@RunWith(MockitoJUnitRunner.class)
public class EmailSendersServiceImplTests {
    @Spy
    SpringTemplateEngine spySpringTemplateEngine;

    @Mock
    JavaMailSender mockMailSender;

    @InjectMocks
    EmailSendersServiceImpl mockService;

    @Test
    public void sendEmailShould_CallJavaMailSender() {
        // Arrange
        Mockito.when(mockMailSender.createMimeMessage()).thenReturn(createMimeMessage());

        // Act
        mockService.sendEmail("to", "from", "subject", "path", createContext());

        // Assert
        Mockito.verify(mockMailSender, Mockito.times(1))
                .send(Mockito.any(MimeMessage.class));
    }

    @Test
    public void sendEmailShould_Trow_WhenContextIsNull() {
        // Arrange, Act, Assert
        Assert.assertThrows(EmailNotSentException.class,
                () -> mockService.sendEmail("to", "from", "subject", "path", null));
    }
}