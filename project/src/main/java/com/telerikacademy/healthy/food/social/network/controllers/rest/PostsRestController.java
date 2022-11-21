package com.telerikacademy.healthy.food.social.network.controllers.rest;

import com.telerikacademy.healthy.food.social.network.models.Post;
import com.telerikacademy.healthy.food.social.network.models.UserDetails;
import com.telerikacademy.healthy.food.social.network.models.dtos.mappers.PostMapper;
import com.telerikacademy.healthy.food.social.network.models.dtos.posts.PostDto;
import com.telerikacademy.healthy.food.social.network.services.contracts.PostsService;
import com.telerikacademy.healthy.food.social.network.services.contracts.UserDetailsService;
import com.telerikacademy.healthy.food.social.network.services.managers.contracts.PostsManager;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.Collection;

import static com.telerikacademy.healthy.food.social.network.utils.GlobalConstants.*;

@RestController
@RequestMapping(API_URL)
public class PostsRestController {
    private final PostsManager postsManager;
    private final PostsService postsService;
    private final UserDetailsService userDetailsService;
    private final PostMapper postMapper;

    @Autowired
    public PostsRestController(PostsManager postsManager, PostsService postsService, UserDetailsService userDetailsService, PostMapper postMapper) {
        this.postsManager = postsManager;
        this.postsService = postsService;
        this.userDetailsService = userDetailsService;
        this.postMapper = postMapper;
    }

    @GetMapping(POSTS_URL)
    @ApiOperation(value = "List of public posts", response = Collection.class)
    public Collection<Post> getPublicPosts(@RequestParam(required = false, defaultValue = "0") int page,
                                           @RequestParam(required = false, defaultValue = "5") int size,
                                           @RequestParam(required = false, defaultValue = "") String title,
                                           @RequestParam(required = false, defaultValue = "0") int category,
                                           @RequestParam(required = false) String sort) {
        Pageable pageable = PageRequest.of(page, size);
        return postsService.getAllPublic(title, category, sort, pageable);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping(POSTS_URL + "/connected")
    @ApiOperation(value = "List of connected users' posts", response = Collection.class)
    public Collection<Post> getConnectedUsersPosts(@RequestParam(required = false, defaultValue = "0") int page,
                                                   @RequestParam(required = false, defaultValue = "5") int size,
                                                   @RequestParam(required = false, defaultValue = "") String title,
                                                   @RequestParam(required = false, defaultValue = "0") int category,
                                                   @RequestParam(required = false) String sort,
                                                   final Authentication auth) {
        Pageable pageable = PageRequest.of(page, size);
        UserDetails logged = userDetailsService.getUserDetailsByEmail(auth.getName());
        return postsService.getAllConnectedUsersPost(title, category, sort, logged, pageable);
    }

    @GetMapping(ADMIN_URL + POSTS_URL)
    @ApiOperation(value = "List of all posts", response = Collection.class)
    public Collection<Post> getAllPosts(@RequestParam(required = false, defaultValue = "0") int page,
                                        @RequestParam(required = false, defaultValue = "5") int size,
                                        @RequestParam(required = false, defaultValue = "") String title,
                                        @RequestParam(required = false, defaultValue = "0") int category,
                                        @RequestParam(required = false) String sort) {
        Pageable pageable = PageRequest.of(page, size);
        return postsService.getAll(title, category, sort, pageable);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping(POSTS_URL + "/my")
    @ApiOperation(value = "List of my posts", response = Collection.class)
    public Collection<Post> getMyPosts(@RequestParam int page, @RequestParam int size, final Authentication auth) {
        Pageable pageable = PageRequest.of(page, size);
        UserDetails logged = userDetailsService.getUserDetailsByEmail(auth.getName());
        return postsService.getAllUserPosts(logged, pageable);
    }

    @GetMapping(POSTS_URL + "/top")
    @ApiOperation(value = "List of top 3 posts", response = Collection.class)
    public Collection<Post> getTop3Posts() {
        return postsService.getTop3Posts();
    }

    @GetMapping(POSTS_URL + "/{id}")
    @ApiOperation(value = "Get post by id", response = Post.class)
    public Post getPostById(@PathVariable long id, final Authentication auth) {
        if (auth == null) {
            return postsManager.getPublicPostById(id);
        }
        UserDetails logged = userDetailsService.getUserDetailsByEmail(auth.getName());
        return postsManager.getPostById(id, logged);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping(POSTS_URL)
    @ApiOperation(value = "Create post", response = Post.class)
    public Post createPost(@RequestPart(required = false) MultipartFile file,
                           @RequestPart("post") @Valid PostDto dto,
                           final Authentication auth) {
        dto.setFile(file);
        Post post = postMapper.toPost(dto);
        post.setCreator(userDetailsService.getUserDetailsByEmail(auth.getName()));
        return postsService.savePost(post);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PutMapping(POSTS_URL + "/{id}")
    @ApiOperation(value = "Update post", response = Post.class)
    public Post updatePost(@PathVariable long id,
                           @RequestPart(required = false) MultipartFile file,
                           @RequestPart("post") @Valid PostDto dto,
                           final Authentication auth) {
        UserDetails logged = userDetailsService.getUserDetailsByEmail(auth.getName());
        Post oldPost = postsService.getPostById(id);
        dto.setFile(file);
        Post post = postMapper.mergePost(oldPost, dto);
        return postsManager.updatePost(post, logged);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @DeleteMapping(POSTS_URL + "/{id}")
    @ApiOperation(value = "Delete post", notes = "Return list of all posts", response = Collection.class)
    public Collection<Post> deletePost(@PathVariable int id, final Authentication auth) {
        UserDetails logged = userDetailsService.getUserDetailsByEmail(auth.getName());
        Post post = postsService.getPostById(id);
        postsManager.deletePost(post, logged);
        return postsService.getAllUserPosts(logged, Pageable.unpaged());
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping(POSTS_URL + "/{id}/likes")
    @ApiOperation(value = "Show post`s likes", response = Post.class)
    public long getPostsLikes(@PathVariable long id, Authentication auth) {
        UserDetails logged = userDetailsService.getUserDetailsByEmail(auth.getName());
        return postsManager.getPostsLikes(id, logged);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping(POSTS_URL + "/{id}/likes")
    @ApiOperation(value = "Like post", response = Post.class)
    public long likePost(@PathVariable long id, Authentication auth) {
        UserDetails logged = userDetailsService.getUserDetailsByEmail(auth.getName());
        return postsManager.likePost(id, logged).getLikedUsers().size();
    }
}
