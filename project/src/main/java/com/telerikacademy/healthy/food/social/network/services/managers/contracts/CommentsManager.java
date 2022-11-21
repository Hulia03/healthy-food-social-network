package com.telerikacademy.healthy.food.social.network.services.managers.contracts;

import com.telerikacademy.healthy.food.social.network.models.Comment;
import com.telerikacademy.healthy.food.social.network.models.UserDetails;

import java.util.Collection;

public interface CommentsManager {
    Collection<Comment> getAll(long id, UserDetails logged);

    Comment getCommentById(long id, UserDetails logged);

    Comment getPublicCommentById(long id);

    Comment createComment(long postId, Comment comment, UserDetails logged);

    Comment updateComment(Comment comment, UserDetails logged);

    void deleteComment(Comment comment, UserDetails logged);

    long getCommentsLikes(long id, UserDetails logged);

    Comment likeComment(long id, UserDetails logged);

    boolean isCommentAlreadyLikedByUser(UserDetails logged, Comment comment);

}
