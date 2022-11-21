package com.telerikacademy.healthy.food.social.network.services.managers.contracts;

import com.telerikacademy.healthy.food.social.network.models.Post;
import com.telerikacademy.healthy.food.social.network.models.UserDetails;
import org.springframework.data.domain.Pageable;

import java.util.Collection;

public interface PostsManager {
    Collection<Post> getAllUserPosts(UserDetails user, UserDetails logged, Pageable pageable);

    Post getPublicPostById(long id);

    Post getPostById(long id, UserDetails logged);

    Post updatePost(Post post, UserDetails logged);

    void deletePost(Post post, UserDetails logged);

    long getPostsLikes(long id, UserDetails logged);

    Post likePost(long id, UserDetails logged);

    boolean checkAccessToPost(Post post, UserDetails logged);

    boolean checkPostPublic(long id);

    boolean isPostAlreadyLikedByUser(UserDetails logged, Post post);

}
