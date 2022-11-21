package com.telerikacademy.healthy.food.social.network.services.managers;

import com.telerikacademy.healthy.food.social.network.models.Comment;
import com.telerikacademy.healthy.food.social.network.models.Post;
import com.telerikacademy.healthy.food.social.network.models.UserDetails;
import com.telerikacademy.healthy.food.social.network.services.contracts.CommentsService;
import com.telerikacademy.healthy.food.social.network.services.contracts.UserDetailsService;
import com.telerikacademy.healthy.food.social.network.services.managers.contracts.CommentsManager;
import com.telerikacademy.healthy.food.social.network.services.managers.contracts.PostsManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import static com.telerikacademy.healthy.food.social.network.utils.GlobalConstants.*;
import static com.telerikacademy.healthy.food.social.network.utils.GlobalConstants.CANNOT_MODIFY_COMMENT_MESSAGE_FORMAT;

@Component
public class CommentsManagerImpl implements CommentsManager {
    private final PostsManager postsManager;
    private final CommentsService commentsService;
    private final UserDetailsService userDetailsService;

    @Autowired
    public CommentsManagerImpl(PostsManager postsManager, CommentsService commentsService, UserDetailsService userDetailsService) {
        this.postsManager = postsManager;
        this.commentsService = commentsService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    @Transactional
    public Collection<Comment> getAll(long id, UserDetails logged) {
        Post post = postsManager.getPostById(id, logged);
        return commentsService.getAll(post);
    }

    @Override
    public Comment getCommentById(long id, UserDetails logged) {
        Comment comment = commentsService.getCommentById(id);
        if (comment.getPost() == null || !postsManager.checkAccessToPost(comment.getPost(), logged)) {
            throw new AccessDeniedException(String.format(CANNOT_ACCESS_POST_MESSAGE_FORMAT, id));
        }

        return comment;
    }

    @Override
    public Comment getPublicCommentById(long id) {
        Comment comment = commentsService.getCommentById(id);
        if (comment.getPost() == null || !postsManager.checkPostPublic(comment.getPost().getId())) {
            throw new AccessDeniedException(String.format(CANNOT_ACCESS_POST_MESSAGE_FORMAT, id));
        }

        return comment;
    }

    @Override
    @Transactional
    public Comment createComment(long postId, Comment comment, UserDetails logged) {
        comment.setPost(postsManager.getPostById(postId, logged));
        return commentsService.saveComment(comment);
    }

    @Override
    public Comment updateComment(Comment comment, UserDetails logged) {
        throwExceptionWhenLoggedIsNotAuthorize(logged.getId(), comment.getId());
        return commentsService.saveComment(comment);
    }

    @Override
    public void deleteComment(Comment comment, UserDetails logged) {
        throwExceptionWhenLoggedIsNotAuthorize(logged.getId(), comment.getId());
        commentsService.deleteComment(comment);
    }

    @Override
    public long getCommentsLikes(long id, UserDetails logged) {
        Comment comment = getCommentById(id, logged);
        return comment.getLikedUsers().size();
    }

    @Override
    @Transactional
    public Comment likeComment(long id, UserDetails logged) {
        Comment comment = getCommentById(id, logged);
        if (isCommentAlreadyLikedByUser(logged, comment)) {
            Set<UserDetails> updated = comment.getLikedUsers().stream()
                    .filter(u -> !u.getEmail().equals(logged.getEmail()))
                    .collect(Collectors.toSet());
            comment.setLikedUsers(updated);
        } else {
            comment.getLikedUsers().add(logged);
        }

        return commentsService.saveComment(comment);
    }

    @Override
    public boolean isCommentAlreadyLikedByUser(UserDetails logged, Comment comment) {
        return comment.getLikedUsers().stream().anyMatch(u -> u.getEmail().equals(logged.getEmail()));
    }

    private void throwExceptionWhenLoggedIsNotAuthorize(long userId, long commentId) {
        boolean isAdmin = userDetailsService.adminPermission(userId);
        if(!isAdmin && userId != commentId){
            throw new AccessDeniedException(String.format(CANNOT_MODIFY_COMMENT_MESSAGE_FORMAT, commentId));
        }
    }
}
