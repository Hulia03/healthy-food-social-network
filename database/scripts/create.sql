-- Depending on your setup local/remote or one/many databases you may or may not need to select db:
-- use my-remote-db-example;

create table categories
(
    category_id int auto_increment
        primary key,
    emoji       varchar(100)         not null,
    enabled     tinyint(1) default 1 not null,
    name        varchar(50)          not null,
    constraint emoticons_emoji_uindex
        unique (emoji)
);

create table gender
(
    gender_id int auto_increment
        primary key,
    type      varchar(50)          not null,
    enabled   tinyint(1) default 1 not null,
    constraint gender_type_uindex
        unique (type)
);

create table media_types
(
    type_id int auto_increment
        primary key,
    type    varchar(50)          not null,
    enabled tinyint(1) default 1 not null,
    constraint media_types_name_uindex
        unique (type)
);

create table nationalities
(
    nationality_id int auto_increment
        primary key,
    nationality    varchar(50)          not null,
    enabled        tinyint(1) default 1 not null,
    constraint nationalities_nationality_uindex
        unique (nationality)
);

create table statuses
(
    status_id int auto_increment
        primary key,
    type      varchar(50)          not null,
    enabled   tinyint(1) default 1 not null,
    constraint statuses_type_uindex
        unique (type)
);

create table users
(
    username varchar(100)         not null,
    password varchar(68)          not null,
    enabled  tinyint(1) default 0 not null,
    constraint users_username_uindex
        unique (username)
);

create table authorities
(
    username  varchar(100) not null,
    authority varchar(50)  not null,
    constraint authorities_pk
        unique (username, authority),
    constraint authorities_users_username_fk
        foreign key (username) references users (username)
);

create table confirmation_tokens
(
    confirmation_token_id bigint auto_increment
        primary key,
    confirmation_token    varchar(255) null,
    created_date          datetime     null,
    username              varchar(100) not null,
    constraint confirmation_tokens_users_username_fk
        foreign key (username) references users (username)
);

create table visibilities
(
    visibility_id int auto_increment
        primary key,
    type          varchar(50)          not null,
    enabled       tinyint(1) default 1 not null,
    constraint visabilities_type_uindex
        unique (type)
);

create table media
(
    media_id      bigint auto_increment
        primary key,
    type_id       int                  not null,
    visibility_id int                  not null,
    path          varchar(200)         not null,
    enabled       tinyint(1) default 1 not null,
    constraint media_media_types_type_id_fk
        foreign key (type_id) references media_types (type_id),
    constraint user_profile_pictures_visabilities_visability_id_fk
        foreign key (visibility_id) references visibilities (visibility_id)
);

create table users_details
(
    user_details_id bigint auto_increment
        primary key,
    email           varchar(100)         not null,
    first_name      varchar(50)          not null,
    last_name       varchar(50)          not null,
    picture_id      bigint               not null,
    description     varchar(500)         null,
    age             int                  null,
    gender_id       int                  null,
    nationality_id  int                  null,
    enabled         tinyint(1) default 1 not null,
    constraint users_details_email_uindex
        unique (email),
    constraint users_details_gender_gender_id_fk
        foreign key (gender_id) references gender (gender_id),
    constraint users_details_medias_media_id_fk
        foreign key (picture_id) references media (media_id),
    constraint users_details_nationalities_nationality_id_fk
        foreign key (nationality_id) references nationalities (nationality_id),
    constraint users_details_users_username_fk
        foreign key (email) references users (username)
);

create table connections
(
    connection_id bigint auto_increment
        primary key,
    sender_id     bigint not null,
    receiver_id   bigint not null,
    status_id     int    not null,
    constraint connections_pk_2
        unique (sender_id, receiver_id),
    constraint connections_statuses_status_id_fk
        foreign key (status_id) references statuses (status_id),
    constraint connections_users_details_user_details_id_fk
        foreign key (sender_id) references users_details (user_details_id),
    constraint connections_users_details_user_details_id_fk_2
        foreign key (receiver_id) references users_details (user_details_id)
);

create table posts
(
    post_id       bigint auto_increment
        primary key,
    visibility_id int                          not null,
    creator_id    bigint                       not null,
    title         varchar(100)                 not null,
    description   longtext                     null,
    media_id      bigint                       null,
    -- timestamp     datetime   default curtime() null,
    timestamp     timestamp   default current_timestamp null,
    enabled       tinyint(1) default 1         null,
    constraint posts_media_media_id_fk
        foreign key (media_id) references media (media_id),
    constraint posts_users_details_user_details_id_fk
        foreign key (creator_id) references users_details (user_details_id),
    constraint posts_visabilities_visability_id_fk
        foreign key (visibility_id) references visibilities (visibility_id)
);

create table comments
(
    comment_id  bigint auto_increment
        primary key,
    post_id     bigint                       not null,
    creator_id  bigint                       not null,
    description longtext                     not null,
    timestamp   timestamp   default current_timestamp not null,
    enabled     tinyint(1) default 1         not null,
    constraint comments_posts_post_id_fk
        foreign key (post_id) references posts (post_id),
    constraint comments_users_details_user_details_id_fk
        foreign key (creator_id) references users_details (user_details_id)
);

create table comments_likes
(
    like_id         bigint auto_increment
        primary key,
    user_details_id bigint not null,
    comment_id      bigint not null,
    constraint likes_pk_2
        unique (user_details_id, comment_id),
    constraint comments_likes_comments_comment_id_fk
        foreign key (comment_id) references comments (comment_id),
    constraint likes_users_details_user_details_id_fk
        foreign key (user_details_id) references users_details (user_details_id)
);

create table posts_categories
(
    id          bigint auto_increment
        primary key,
    post_id     bigint not null,
    category_id int    not null,
    constraint posts_categories_categories_category_id_fk
        foreign key (category_id) references categories (category_id),
    constraint posts_categories_posts_post_id_fk
        foreign key (post_id) references posts (post_id)
);

create table posts_likes
(
    id              bigint auto_increment
        primary key,
    user_details_id bigint not null,
    post_id         bigint not null,
    constraint posts_likes_pk_2
        unique (user_details_id, post_id),
    constraint posts_likes_posts_post_id_fk
        foreign key (post_id) references posts (post_id),
    constraint posts_likes_users_details_user_details_id_fk
        foreign key (user_details_id) references users_details (user_details_id)
);


