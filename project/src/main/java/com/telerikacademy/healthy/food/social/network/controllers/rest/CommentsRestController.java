package com.telerikacademy.healthy.food.social.network.controllers.rest;

import com.telerikacademy.healthy.food.social.network.models.Comment;
import com.telerikacademy.healthy.food.social.network.models.Post;
import com.telerikacademy.healthy.food.social.network.models.UserDetails;
import com.telerikacademy.healthy.food.social.network.models.dtos.comments.CommentDto;
import com.telerikacademy.healthy.food.social.network.services.contracts.CommentsService;
import com.telerikacademy.healthy.food.social.network.services.contracts.UserDetailsService;
import com.telerikacademy.healthy.food.social.network.services.managers.contracts.CommentsManager;
import com.telerikacademy.healthy.food.social.network.services.managers.contracts.PostsManager;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.sql.Timestamp;
import java.util.Collection;

import static com.telerikacademy.healthy.food.social.network.utils.GlobalConstants.*;

@RestController
@RequestMapping(API_URL)
public class CommentsRestController {
    private final CommentsManager commentsManager;
    private final CommentsService commentsService;
    private final PostsManager postsManager;
    private final UserDetailsService userDetailsService;

    @Autowired
    public CommentsRestController(CommentsManager commentsManager, CommentsService commentsService, PostsManager postsManager, UserDetailsService userDetailsService) {
        this.commentsManager = commentsManager;
        this.commentsService = commentsService;
        this.postsManager = postsManager;
        this.userDetailsService = userDetailsService;

    }

    @GetMapping(POSTS_URL + "/{id}" + COMMENTS_URL)
    @ApiOperation(value = "List of all comments", response = Collection.class)
    public Collection<Comment> getComments(@PathVariable int id, Authentication auth) {
        if (auth == null) {
            Post post = postsManager.getPublicPostById(id);
            return commentsService.getAll(post);
        }

        UserDetails logged = userDetailsService.getUserDetailsByEmail(auth.getName());
        return commentsManager.getAll(id, logged);
    }

    @GetMapping(POSTS_URL + "/{id}/latest_comments")
    @ApiOperation(value = "List of latest 5 comments", response = Collection.class)
    public Collection<Comment> getLatest5Comments(@PathVariable int id, Authentication auth) {
        if (auth == null) {
            Post post = postsManager.getPublicPostById(id);
            return commentsService.getLatest5(post);
        }

        UserDetails logged = userDetailsService.getUserDetailsByEmail(auth.getName());
        Post post = postsManager.getPostById(id, logged);
        return commentsService.getLatest5(post);
    }

    @GetMapping(COMMENTS_URL + "/{id}")
    @ApiOperation(value = "Get comment by id", response = Comment.class)
    public Comment getCommentById(@PathVariable int id, Authentication auth) {
        if (auth == null) {
            return commentsManager.getPublicCommentById(id);
        }

        UserDetails logged = userDetailsService.getUserDetailsByEmail(auth.getName());
        return commentsManager.getCommentById(id, logged);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping(POSTS_URL + "/{id}" + COMMENTS_URL)
    @ApiOperation(value = "Create comment", response = Comment.class)
    public Comment createComment(@PathVariable int id, @RequestBody @Valid CommentDto dto,
                                 Authentication auth) {
        UserDetails logged = userDetailsService.getUserDetailsByEmail(auth.getName());
        Comment comment = new Comment();
        comment.setCreator(logged);
        comment.setDescription(dto.getComment());
        comment.setTimestamp(new Timestamp(System.currentTimeMillis()));
        return commentsManager.createComment(id, comment, logged);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PutMapping(COMMENTS_URL + "/{id}")
    @ApiOperation(value = "Update comment", response = Comment.class)
    public Comment updateComment(@PathVariable int id, @RequestBody @Valid CommentDto dto,
                                 Authentication auth) {
        UserDetails logged = userDetailsService.getUserDetailsByEmail(auth.getName());
        Comment comment = commentsService.getCommentById(id);
        comment.setDescription(dto.getComment());
        return commentsManager.updateComment(comment, logged);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @DeleteMapping(COMMENTS_URL + "/{id}")
    @ApiOperation(value = "Delete comment", response = Collection.class)
    public Collection<Comment> deleteComment(@PathVariable int id, Authentication auth) {
        UserDetails logged = userDetailsService.getUserDetailsByEmail(auth.getName());
        Comment comment = commentsService.getCommentById(id);
        commentsManager.deleteComment(comment, logged);
        return commentsManager.getAll(comment.getPost().getId(), logged);
    }

    @GetMapping(COMMENTS_URL + "/{id}/likes")
    @ApiOperation(value = "Show comment`s likes", response = Post.class)
    public long getCommentsLikes(@PathVariable long id, Authentication auth) {
        UserDetails logged = userDetailsService.getUserDetailsByEmail(auth.getName());
        return commentsManager.getCommentsLikes(id, logged);
    }

    @PostMapping(COMMENTS_URL + "/{id}/likes")
    @ApiOperation(value = "Like comment", response = Post.class)
    public long likeComment(@PathVariable long id, Authentication auth) {
        UserDetails logged = userDetailsService.getUserDetailsByEmail(auth.getName());
        return commentsManager.likeComment(id, logged).getLikedUsers().size();
    }

}
