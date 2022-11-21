package com.telerikacademy.healthy.food.social.network;

import com.telerikacademy.healthy.food.social.network.models.*;
import com.telerikacademy.healthy.food.social.network.models.dtos.PasswordUserDto;
import com.telerikacademy.healthy.food.social.network.utils.GlobalConstants;
import org.springframework.mock.web.MockMultipartFile;
import org.thymeleaf.context.Context;

import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Collections;
import java.util.Locale;
import java.util.Properties;

import static com.telerikacademy.healthy.food.social.network.utils.GlobalConstants.CONNECTED;
import static com.telerikacademy.healthy.food.social.network.utils.GlobalConstants.SENT_REQUEST;

public class Factory {
    public static Visibility createVisibilityPublic() {
        return new Visibility(1, "public");
    }

    public static Visibility createVisibilityConnected() {
        return new Visibility(2, "connected");
    }

    public static Gender createGender() {
        return new Gender(1, "male");
    }

    public static MediaType createMediaType() {
        return new MediaType(1, "video");
    }

    public static Media createMediaPublic() {
        return new Media(1, "url", createMediaType(), createVisibilityPublic());
    }

    public static Media createMediaConnected() {
        return new Media(1, "url", createMediaType(), createVisibilityConnected());
    }

    public static MockMultipartFile createMockMultipartFile() {
        return new MockMultipartFile("name", "test.png", "image/png", "content".getBytes());
    }

    public static Status createStatusConnected() {
        return new Status(1, CONNECTED);
    }

    public static Status createStatusSentRequest() {
        return new Status(1, SENT_REQUEST);
    }

    public static Category createCategoryVegetables() {
        return new Category(1, "vegetables", "url");
    }

    public static Category createCategoryVegan() {
        return new Category(2, "vegan", "url");
    }

    public static Nationality createNationalityBG() {
        return new Nationality(1, "Bulgarian");
    }

    public static Nationality createNationalityEN() {
        return new Nationality(2, "English");
    }

    public static Post createPostPublic() {
        return new Post(1, createVisibilityPublic(), createUserDetails(), "Title", "Description", createMediaPublic(), Collections.emptyList());
    }

    public static Post createPostConnected() {
        return new Post(2, createVisibilityConnected(), createUserDetails(), "Title", "Description", createMediaPublic(), Collections.emptyList());
    }

    public static Post createPostWithLikes() {
        return new Post(1, createVisibilityPublic(), createUserDetails(), "Title", "Description", createMediaPublic(), Arrays.asList(createUserDetails()));
    }

    public static ConfirmationToken createToken() {
        return new ConfirmationToken(1, "Token", new Timestamp(System.currentTimeMillis()), createUser());
    }

    public static UserDetails createUserDetails() {
        return new UserDetails(1, "test@email.com", "Ivan", "Ivanov", createMediaPublic(), true);
    }

    public static UserDetails createSender() {
        return new UserDetails(2, "sender@email.com", "Sender", "Sender", createMediaPublic(), true);
    }

    public static UserDetails createReceiver() {
        return new UserDetails(3, "receiver@email.com", "Receiver", "Receiver", createMediaPublic(), true);
    }

    public static Connection createConnectionSenderReceiver() {
        return new Connection(1, createSender(), createReceiver(), createStatusConnected());
    }

    public static Connection createConnectionReceiverSender() {
        return new Connection(1, createReceiver(), createSender(), createStatusConnected());
    }

    public static User createUser() {
        return new User("email", "password", true);
    }

    public static Comment createComment() {
        return new Comment(1, createPostPublic(), createUserDetails(), "desc", new Timestamp(System.currentTimeMillis()), Collections.emptySet());
    }

    public static Comment createCommentWithLikes() {
        return new Comment(1, createPostPublic(), createUserDetails(), "desc", new Timestamp(System.currentTimeMillis()), Arrays.asList(createUserDetails()));
    }

    public static MimeMessage createMimeMessage() {
        Properties properties = System.getProperties();
        Session session = Session.getDefaultInstance(properties);
        return new MimeMessage(session);
    }

    public static Context createContext() {
        Context context = new Context(Locale.ENGLISH);
        context.setVariable(GlobalConstants.TOKEN, "token");
        return context;
    }

    public static PasswordUserDto createPasswordUserDto() {
        return new PasswordUserDto("anor@abv.bg", "pass", "123456", "123456");
    }
}
