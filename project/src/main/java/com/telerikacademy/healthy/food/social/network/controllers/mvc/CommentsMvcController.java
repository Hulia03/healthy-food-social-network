package com.telerikacademy.healthy.food.social.network.controllers.mvc;

import com.telerikacademy.healthy.food.social.network.models.Comment;
import com.telerikacademy.healthy.food.social.network.models.Post;
import com.telerikacademy.healthy.food.social.network.models.UserDetails;
import com.telerikacademy.healthy.food.social.network.models.dtos.comments.CommentDto;
import com.telerikacademy.healthy.food.social.network.services.contracts.CommentsService;
import com.telerikacademy.healthy.food.social.network.services.contracts.UserDetailsService;
import com.telerikacademy.healthy.food.social.network.services.managers.contracts.CommentsManager;
import com.telerikacademy.healthy.food.social.network.services.managers.contracts.PostsManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.sql.Timestamp;

import static com.telerikacademy.healthy.food.social.network.utils.GlobalConstants.COMMENTS_URL;
import static com.telerikacademy.healthy.food.social.network.utils.GlobalConstants.POSTS_URL;

@Controller
public class CommentsMvcController {
    private final CommentsManager commentsManager;
    private final CommentsService commentsService;
    private final PostsManager postsManager;
    private final UserDetailsService userDetailsService;

    @Autowired
    public CommentsMvcController(CommentsManager commentsManager,
                                 CommentsService commentsService,
                                 PostsManager postsManager,
                                 UserDetailsService userDetailsService) {
        this.commentsManager = commentsManager;
        this.commentsService = commentsService;
        this.postsManager = postsManager;
        this.userDetailsService = userDetailsService;
    }


    @GetMapping(POSTS_URL + "/{id}" + COMMENTS_URL)
    public String showAllCommentToPost(@PathVariable(value = "id") long postId,
                                       final Model model,
                                       final Authentication auth) {
        if (auth == null) {
            Post post = postsManager.getPublicPostById(postId);
            model.addAttribute("comments", commentsService.getAll(post));
        } else {
            UserDetails logged = userDetailsService.getUserDetailsByEmail(auth.getName());
            model.addAttribute("comments", commentsManager.getAll(postId, logged));
            model.addAttribute("dto", new CommentDto());
        }
        return "comments/comments";
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping(POSTS_URL + "/{id}/new")
    public String createComment(@PathVariable(value = "id") long postId, @Valid @ModelAttribute("dto") CommentDto dto,
                                BindingResult errors,
                                final Authentication auth) {
        if (errors.hasErrors()) {
            return "redirect:/posts/" + postId;
        }

        UserDetails logged = userDetailsService.getUserDetailsByEmail(auth.getName());
        Comment comment = new Comment();
        comment.setCreator(logged);
        comment.setDescription(dto.getComment());
        comment.setTimestamp(new Timestamp(System.currentTimeMillis()));
        commentsManager.createComment(postId, comment, logged);
        return "redirect:/posts/" + postId;
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping(COMMENTS_URL + "/{id}/update")
    public String showUpdateCommentForm(@PathVariable long id, final Model model, final Authentication auth) {
        UserDetails logged = userDetailsService.getUserDetailsByEmail(auth.getName());
        Comment oldComment = commentsManager.getCommentById(id, logged);
        CommentDto dto = new CommentDto();
        dto.setComment(oldComment.getDescription());
        model.addAttribute("comment", dto);
        return "comments/update-comment";
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping(COMMENTS_URL + "/{id}/update")
    public String updateComment(@PathVariable long id,
                                @Valid @ModelAttribute("comment") CommentDto dto,
                                BindingResult errors,
                                final Authentication auth) {
        if (errors.hasErrors()) {
            return "comments/update-comment";
        }

        UserDetails logged = userDetailsService.getUserDetailsByEmail(auth.getName());
        Comment oldComment = commentsManager.getCommentById(id, logged);
        oldComment.setDescription(dto.getComment());
        commentsManager.updateComment(oldComment, logged);
        return "redirect:/posts/" + oldComment.getPost().getId();
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping(COMMENTS_URL + "/{id}/delete")
    public String showDeleteCommentForm(@PathVariable long id, final Model model, final Authentication auth) {
        UserDetails logged = userDetailsService.getUserDetailsByEmail(auth.getName());
        Comment comment = commentsManager.getCommentById(id, logged);
        model.addAttribute("comment", comment);
        return "comments/delete-comment";
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping(COMMENTS_URL + "/{id}/delete")
    public String deleteComment(@PathVariable long id,
                                final Authentication auth) {
        UserDetails logged = userDetailsService.getUserDetailsByEmail(auth.getName());
        Comment comment = commentsManager.getCommentById(id, logged);
        commentsManager.deleteComment(comment, logged);
        return "redirect:/posts/" + comment.getPost().getId();

    }
}
