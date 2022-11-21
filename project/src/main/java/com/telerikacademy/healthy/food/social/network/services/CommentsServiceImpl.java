package com.telerikacademy.healthy.food.social.network.services;

import com.telerikacademy.healthy.food.social.network.exceptions.EntityNotFoundException;
import com.telerikacademy.healthy.food.social.network.models.Comment;
import com.telerikacademy.healthy.food.social.network.models.Post;
import com.telerikacademy.healthy.food.social.network.repositories.contracts.CommentsRepository;
import com.telerikacademy.healthy.food.social.network.services.contracts.CommentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

import static com.telerikacademy.healthy.food.social.network.utils.GlobalConstants.COMMENT;

@Service
public class CommentsServiceImpl implements CommentsService {
    private final CommentsRepository commentsRepository;

    @Autowired
    public CommentsServiceImpl(CommentsRepository commentsRepository) {
        this.commentsRepository = commentsRepository;
    }

    @Override
    public Collection<Comment> getAll(Post post) {
        return commentsRepository.findAllByPostOrderByTimestampDesc(post);
    }

    @Override
    public Collection<Comment> getLatest5(Post post) {
        return commentsRepository.findLatest5Comments(post.getId());
    }

    @Override
    public Comment getCommentById(long id) {
        return commentsRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(COMMENT, id)
        );
    }

    @Override
    public Comment saveComment(Comment comment) {
        return commentsRepository.save(comment);
    }

    @Override
    public void deleteComment(Comment comment) {
        commentsRepository.delete(comment);
    }
}
