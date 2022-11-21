package com.telerikacademy.healthy.food.social.network.services.contracts;

import com.telerikacademy.healthy.food.social.network.models.Post;
import com.telerikacademy.healthy.food.social.network.models.UserDetails;
import org.springframework.data.domain.Pageable;

import java.util.Collection;

public interface PostsService {
    Collection<Post> getAll(String title, int categoryId, String sort, Pageable pageable);

    Collection<Post> getAllPublic(String title, int categoryId, String sort, Pageable pageable);

    Collection<Post> getAllConnectedUsersPost(String title, int categoryId, String sort, UserDetails logged, Pageable pageable);

    Collection<Post> getAllUserPosts(UserDetails user, Pageable pageable);

    Collection<Post> getAllPublicUserPosts(UserDetails user, Pageable pageable);

    Collection<Post> getTop3Posts();

    Post getPostById(long id);

    Post savePost(Post post);

    void deletePost(Post post);
}
