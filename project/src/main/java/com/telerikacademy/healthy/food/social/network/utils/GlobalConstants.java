package com.telerikacademy.healthy.food.social.network.utils;

public class GlobalConstants {
    public static final String API_URL = "/api/v1";
    public static final String POSTS_URL = "/posts";
    public static final String ADMIN_URL = "/admin";
    public static final String NATIONALITY_URL = "/nationalities";
    public static final String CATEGORY_URL = "/categories";
    public static final String USERS_URL = "/users";
    public static final String CONNECTIONS_URL = "/connections";
    public static final String COMMENTS_URL = "/comments";
    public static final String HOST_URL = "host";

    public static final String CONFIRMATION_TOKEN = "Confirmation token";
    public static final String GENDER = "Gender";
    public static final String USER = "User";
    public static final String MEDIA = "Media";
    public static final String CATEGORY = "Category";
    public static final String STATUS = "Status";
    public static final String POST = "Post";
    public static final String MEDIA_TYPE = "Media type";
    public static final String VISIBILITY = "Visibility";
    public static final String NATIONALITY = "Nationality";
    public static final String COMMENT = "Comment";
    public static final String NAME = "name";
    public static final String EMAIL = "email";
    public static final String TYPE = "type";
    public static final String TOKEN = "token";
    public static final String CONNECTION = "Connection";
    public static final String CONNECTED = "connected";
    public static final String SENT_REQUEST = "sent request";
    public static final String PUBLIC = "public";
    public static final String LIKES = "likes";
    public static final String COMMENTS = "comments";
    public static final String TITLE = "title";

    public static final int USER_NAME_MIN_LENGTH = 2;
    public static final int USER_NAME_MAX_LENGTH = 50;
    public static final String USER_LAST_NAME_MESSAGE_ERROR = "User's last name should be between " + USER_NAME_MIN_LENGTH + " and " + USER_NAME_MAX_LENGTH + " characters.";
    public static final String USER_FIRST_NAME_MESSAGE_ERROR = "User's first name should be between " + USER_NAME_MIN_LENGTH + " and " + USER_NAME_MAX_LENGTH + " characters.";
    public static final String USER_EMAIL_MESSAGE_ERROR = "Not valid email format.";
    public static final String USER_PASSWORD_MESSAGE_ERROR = "Password should be at least 8 characters and should contain a minimum 1 lower case letter, 1 upper case letter, 1 numeric character and a special character: [@,#,$,%,!]";
    public static final int USER_DESCRIPTION_MAX_LENGTH = 500;
    public static final String USER_DESCRIPTION_MESSAGE_ERROR = "User's description should be less than" + USER_DESCRIPTION_MAX_LENGTH + " characters.";
    public static final String USER_AGE_MESSAGE_ERROR = "Age should be positive.";

    public static final int POST_TITLE_MIN_LEN = 2;
    public static final int POST_TITLE_MAX_LEN = 100;
    public static final String POST_TITLE_MESSAGE_ERROR = "Post's title should be between " + POST_TITLE_MIN_LEN + " and " + POST_TITLE_MAX_LEN + " characters.";

    public static final int CATEGORY_NAME_MIN_LEN = 2;
    public static final int CATEGORY_NAME_MAX_LEN = 50;
    public static final String CATEGORY_NAME_MESSAGE_ERROR = "Category`s name should be between " + CATEGORY_NAME_MIN_LEN + " and " + CATEGORY_NAME_MAX_LEN + " characters.";

    public static final int NATIONALITY_NAME_MIN_LEN = 2;
    public static final int NATIONALITY_NAME_MAX_LEN = 50;
    public static final String NATIONALITY_NAME_MESSAGE_ERROR = "Nationality`s name should be between " + NATIONALITY_NAME_MIN_LEN + " and " + NATIONALITY_NAME_MAX_LEN + " characters.";

    public static final String VISIBILITY_REQUIRED_MESSAGE_ERROR = "Visibility's type is required.";
    public static final String FILE_REQUIRED_MESSAGE_ERROR = "File is required.";

    public static final String COMMENT_MESSAGE_ERROR = "Comment can not be empty";

    public static final String FILE_WAS_NOT_SAVED = "File was not saved: %s";
    public static final String USER_WITH_THE_SAME_USERNAME_ALREADY_EXISTS = "User with the same username already exists!";
    public static final String PASSWORD_DOESN_T_MATCH = "Password does't match!";
    public static final String CONFIRM_RESET_PASSWORD = "Reset password confirmation";
    public static final String COMPLETE_REGISTRATION = "Email confirmation";
    public static final String CONFIRM_PASSWORD_REQUIRED_MESSAGE_ERROR = "Confirm password is required.";
    public static final String USER_S_OLD_PASSWORD_DOESN_T_MATCH = "User's old password doesn't match!";
    public static final String UNKNOWN_ERROR = "Unknown error.";
    public static final String USER_WITH_THIS_EMAIL_DOES_NOT_EXIST = "User with this email does not exist!";
    public static final String CANNOT_ACCESS_POST_MESSAGE_FORMAT = "Cannot access post with Id %d.";
    public static final String CANNOT_MODIFY_POST_MESSAGE_FORMAT = "Cannot modify post with Id %d.";
    public static final String CANNOT_MODIFY_COMMENT_MESSAGE_FORMAT = "Cannot modify comment with Id %d.";
    public static final String CANNOT_MODIFY_USER_MESSAGE_FORMAT = "Cannot modify user`s details with Id %d.";
    public static final String CANNOT_MODIFY_CONNECTION_MESSAGE_FORMAT = "Cannot modify connection with Id %d.";
    public static final String YOU_ARE_ALREADY_CONNECTED_WITH_YOURSELF = "You are already connected with yourself.";
    public static final String ALREADY_CONNECTED_FORMAT_MESSAGE = "You are connected with user with username %s.";
    public static final String REQUEST_IS_ALREADY_SENT = "Request is already send.";
}
