package com.telerikacademy.healthy.food.social.network.services.contracts;

import com.telerikacademy.healthy.food.social.network.models.Comment;
import com.telerikacademy.healthy.food.social.network.models.Post;

import java.util.Collection;

public interface CommentsService {
    Collection<Comment> getAll(Post post);

    Collection<Comment> getLatest5(Post post);

    Comment getCommentById(long id);

    Comment saveComment(Comment comment);

    void deleteComment(Comment comment);
}
