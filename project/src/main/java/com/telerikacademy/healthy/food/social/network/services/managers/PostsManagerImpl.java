package com.telerikacademy.healthy.food.social.network.services.managers;

import com.telerikacademy.healthy.food.social.network.models.Post;
import com.telerikacademy.healthy.food.social.network.models.UserDetails;
import com.telerikacademy.healthy.food.social.network.services.contracts.ConnectionsService;
import com.telerikacademy.healthy.food.social.network.services.contracts.PostsService;
import com.telerikacademy.healthy.food.social.network.services.contracts.UserDetailsService;
import com.telerikacademy.healthy.food.social.network.services.contracts.VisibilitiesService;
import com.telerikacademy.healthy.food.social.network.services.managers.contracts.PostsManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import static com.telerikacademy.healthy.food.social.network.utils.GlobalConstants.CANNOT_ACCESS_POST_MESSAGE_FORMAT;
import static com.telerikacademy.healthy.food.social.network.utils.GlobalConstants.CANNOT_MODIFY_POST_MESSAGE_FORMAT;

@Component
public class PostsManagerImpl implements PostsManager {
    private final PostsService postsService;
    private final UserDetailsService userDetailsService;
    private final ConnectionsService connectionsService;
    private final VisibilitiesService visibilitiesService;

    @Autowired
    public PostsManagerImpl(PostsService postsService, UserDetailsService userDetailsService, ConnectionsService connectionsService, VisibilitiesService visibilitiesService) {
        this.postsService = postsService;
        this.userDetailsService = userDetailsService;
        this.connectionsService = connectionsService;
        this.visibilitiesService = visibilitiesService;
    }

    @Override
    public Collection<Post> getAllUserPosts(UserDetails user, UserDetails logged, Pageable pageable) {
        if (connectionsService.checkUsersConnected(user, logged)
                || userDetailsService.adminPermission(logged.getId())) {
            return postsService.getAllUserPosts(user, pageable);
        }

        return postsService.getAllPublicUserPosts(user, pageable);
    }

    @Override
    public Post getPublicPostById(long id) {
        Post post = postsService.getPostById(id);
        if (!visibilitiesService.checkPostPublic(post)) {
            throw new AccessDeniedException(String.format(CANNOT_ACCESS_POST_MESSAGE_FORMAT, id));
        }

        return post;
    }

    @Override
    public Post getPostById(long id, UserDetails logged) {
        Post post = postsService.getPostById(id);
        if (checkAccessToPost(post, logged)) {
            return post;
        }

        throw new AccessDeniedException(String.format(CANNOT_ACCESS_POST_MESSAGE_FORMAT, id));
    }

    @Override
    public Post updatePost(Post post, UserDetails logged) {
        throwExceptionWhenLoggedIsNotAuthorize(logged.getId(), post.getId());
        return postsService.savePost(post);
    }

    @Override
    public void deletePost(Post post, UserDetails logged) {
        throwExceptionWhenLoggedIsNotAuthorize(logged.getId(), post.getId());
        postsService.deletePost(post);
    }

    @Override
    public long getPostsLikes(long id, UserDetails logged) {
        Post post = getPostById(id, logged);
        return post.getLikedUsers().size();
    }

    @Override
    @Transactional
    public Post likePost(long id, UserDetails logged) {
        Post post = getPostById(id, logged);
        if (isPostAlreadyLikedByUser(logged, post)) {
            Set<UserDetails> updated = post.getLikedUsers().stream().filter(u -> !u.getEmail().equals(logged.getEmail())).collect(Collectors.toSet());
            post.setLikedUsers(updated);
        } else {
            post.getLikedUsers().add(logged);
        }

        return postsService.savePost(post);
    }

    @Override
    public boolean checkAccessToPost(Post post, UserDetails logged) {
        return visibilitiesService.checkPostPublic(post)
                || connectionsService.checkUsersConnected(logged, post.getCreator())
                || userDetailsService.adminPermission(logged.getId());
    }

    @Override
    public boolean checkPostPublic(long id) {
        Post post = postsService.getPostById(id);
        return visibilitiesService.checkPostPublic(post);
    }

    @Override
    public boolean isPostAlreadyLikedByUser(UserDetails logged, Post post) {
        return post.getLikedUsers().stream().anyMatch(u -> u.getEmail().equals(logged.getEmail()));
    }

    private void throwExceptionWhenLoggedIsNotAuthorize(long userId, long postId) {
        boolean isAdmin = userDetailsService.adminPermission(userId);
        if(!isAdmin && userId != postId){
            throw new AccessDeniedException(String.format(CANNOT_MODIFY_POST_MESSAGE_FORMAT, postId));
        }
    }
}
