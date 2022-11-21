package com.telerikacademy.healthy.food.social.network.controllers.mvc;

import com.telerikacademy.healthy.food.social.network.models.Category;
import com.telerikacademy.healthy.food.social.network.models.Post;
import com.telerikacademy.healthy.food.social.network.models.UserDetails;
import com.telerikacademy.healthy.food.social.network.models.Visibility;
import com.telerikacademy.healthy.food.social.network.models.dtos.comments.CommentDto;
import com.telerikacademy.healthy.food.social.network.models.dtos.mappers.PostMapper;
import com.telerikacademy.healthy.food.social.network.models.dtos.posts.PostDto;
import com.telerikacademy.healthy.food.social.network.services.contracts.*;
import com.telerikacademy.healthy.food.social.network.services.managers.contracts.PostsManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static com.telerikacademy.healthy.food.social.network.utils.GlobalConstants.ADMIN_URL;
import static com.telerikacademy.healthy.food.social.network.utils.GlobalConstants.POSTS_URL;


@Controller
public class PostsMvcController {
    private final PostsManager postsManager;
    private final PostsService postsService;
    private final PostMapper postMapper;
    private final CommentsService commentsService;
    private final UserDetailsService userDetailsService;
    private final CategoriesService categoriesService;
    private final VisibilitiesService visibilityService;

    @Autowired
    public PostsMvcController(PostsManager postsManager,
                              PostsService postsService,
                              PostMapper postMapper,
                              CommentsService commentsService,
                              UserDetailsService userDetailsService,
                              CategoriesService categoriesService,
                              VisibilitiesService visibilityService) {
        this.postsManager = postsManager;
        this.postsService = postsService;
        this.postMapper = postMapper;
        this.commentsService = commentsService;
        this.userDetailsService = userDetailsService;
        this.categoriesService = categoriesService;
        this.visibilityService = visibilityService;
    }

    @GetMapping(POSTS_URL)
    public String showPosts() {
        return "posts/feed";
    }

    @GetMapping(POSTS_URL + "/paging")
    public String showPublicPosts(@RequestParam(required = false, defaultValue = "0") int page,
                                  @RequestParam(required = false, defaultValue = "5") int size,
                                  @RequestParam(required = false, defaultValue = "") String title,
                                  @RequestParam(required = false, defaultValue = "0") int category,
                                  @RequestParam(required = false, defaultValue = "") String sort,
                                  final Model model) {
        Pageable pageable = PageRequest.of(page, size);
        model.addAttribute("posts", postsService.getAllPublic(title, category, sort, pageable));
        return "fragments/post :: posts";
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping(POSTS_URL + "/connected")
    public String showConnectedUsersPosts() {
        return "posts/connected-feed";
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping(POSTS_URL + "/connected/paging")
    public String showConnectedUsersPosts(@RequestParam(required = false, defaultValue = "0") int page,
                                          @RequestParam(required = false, defaultValue = "5") int size,
                                          @RequestParam(required = false, defaultValue = "") String title,
                                          @RequestParam(required = false, defaultValue = "0") int category,
                                          @RequestParam(required = false, defaultValue = "") String sort,
                                          final Model model, final Authentication auth) {
        Pageable pageable = PageRequest.of(page, size);
        UserDetails logged = userDetailsService.getUserDetailsByEmail(auth.getName());
        model.addAttribute("posts", postsService.getAllConnectedUsersPost(title, category, sort, logged, pageable));
        return "fragments/post :: posts";
    }

    @GetMapping(ADMIN_URL + POSTS_URL)
    public String showAllPosts() {
        return "posts/admin-feed";
    }

    @GetMapping(ADMIN_URL + POSTS_URL + "/paging")
    public String showAllPosts(@RequestParam(required = false, defaultValue = "0") int page,
                               @RequestParam(required = false, defaultValue = "5") int size,
                               @RequestParam(required = false, defaultValue = "") String title,
                               @RequestParam(required = false, defaultValue = "0") int category,
                               @RequestParam(required = false, defaultValue = "") String sort,
                               final Model model) {
        Pageable pageable = PageRequest.of(page, size);
        model.addAttribute("posts", postsService.getAll(title, category, sort, pageable));
        return "fragments/post :: posts";
    }

    @GetMapping(POSTS_URL + "/{id}")
    public String showPostInfo(@PathVariable long id,
                               final Model model,
                               final Authentication auth) {
        Post post;
        if (auth == null) {
            post = postsManager.getPublicPostById(id);
            model.addAttribute("isLiked", false);
        } else {
            UserDetails logged = userDetailsService.getUserDetailsByEmail(auth.getName());
            post = postsManager.getPostById(id, logged);
            model.addAttribute("dto", new CommentDto());
            model.addAttribute("isLiked", postsManager.isPostAlreadyLikedByUser(logged, post));
        }
        model.addAttribute("post", post);
        model.addAttribute("comments", commentsService.getLatest5(post));

        return "posts/post";
    }

    @GetMapping(POSTS_URL + "/users/{id}/paging")
    public String showUserPosts(@PathVariable long id,
                                @RequestParam(required = false, defaultValue = "0") int page,
                                @RequestParam(required = false, defaultValue = "5") int size,
                                final Model model, final Authentication auth) {
        Pageable pageable = PageRequest.of(page, size);
        UserDetails userDetails = userDetailsService.getUserDetailsById(id);
        Set<Post> posts = new HashSet<>();
        if (auth == null) {
            posts.addAll(postsService.getAllPublicUserPosts(userDetails, pageable));
        } else {
            UserDetails logged = userDetailsService.getUserDetailsByEmail(auth.getName());
            posts.addAll(postsManager.getAllUserPosts(userDetails, logged, pageable));
        }

        model.addAttribute("posts", posts);
        return "fragments/post :: posts";
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping(POSTS_URL + "/new")
    public String showNewPostForm(final Model model) {
        model.addAttribute("post", new PostDto());
        return "posts/create-post";
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping(POSTS_URL + "/new")
    public String createPost(@Valid @ModelAttribute("post") PostDto dto,
                             BindingResult errors,
                             final Authentication auth) {
        if (errors.hasErrors()) {
            return "posts/create-post";
        }

        Post post = postMapper.toPost(dto);
        post.setCreator(userDetailsService.getUserDetailsByEmail(auth.getName()));
        postsService.savePost(post);
        return "redirect:/users/profile";
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping(POSTS_URL + "/{id}/update")
    public String showUpdatePostForm(@PathVariable long id, final Model model, final Authentication auth) {
        UserDetails logged = userDetailsService.getUserDetailsByEmail(auth.getName());
        Post post = postsManager.getPostById(id, logged);
        PostDto dto = postMapper.toPostDto(post);
        model.addAttribute("post", dto);
        return "posts/update-post";
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping(POSTS_URL + "/{id}/update")
    public String updatePost(@PathVariable long id,
                             @Valid @ModelAttribute("post") PostDto dto,
                             BindingResult errors,
                             final Authentication auth) {
        if (errors.hasErrors()) {
            return "posts/update-post";
        }

        UserDetails logged = userDetailsService.getUserDetailsByEmail(auth.getName());
        Post oldPost = postsService.getPostById(id);
        Post post = postMapper.mergePost(oldPost, dto);
        postsManager.updatePost(post, logged);
        return "redirect:/posts/" + id;
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping(POSTS_URL + "/{id}/delete")
    public String showDeletePostForm(@PathVariable long id, final Model model, final Authentication auth) {
        UserDetails logged = userDetailsService.getUserDetailsByEmail(auth.getName());
        Post post = postsManager.getPostById(id, logged);
        model.addAttribute("post", post);
        return "posts/delete-post";
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping(POSTS_URL + "/{id}/delete")
    public String deletePost(@PathVariable long id,
                             final Authentication auth) {
        UserDetails logged = userDetailsService.getUserDetailsByEmail(auth.getName());
        Post post = postsService.getPostById(id);
        postsManager.deletePost(post, logged);
        return "redirect:/users/" + logged.getId();
    }

    @ModelAttribute("categories")
    public Collection<Category> populateCategories() {
        return categoriesService.getAll();
    }

    @ModelAttribute("visibilities")
    public Collection<Visibility> populateVisibilities() {
        return visibilityService.getAll();
    }
}
