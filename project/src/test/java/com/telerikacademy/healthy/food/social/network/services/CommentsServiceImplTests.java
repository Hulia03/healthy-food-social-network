package com.telerikacademy.healthy.food.social.network.services;

import com.telerikacademy.healthy.food.social.network.exceptions.EntityNotFoundException;
import com.telerikacademy.healthy.food.social.network.models.Comment;
import com.telerikacademy.healthy.food.social.network.models.Post;
import com.telerikacademy.healthy.food.social.network.repositories.contracts.CommentsRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

import static com.telerikacademy.healthy.food.social.network.Factory.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.times;

@RunWith(MockitoJUnitRunner.class)
public class CommentsServiceImplTests {
    @Mock
    CommentsRepository mockRepository;

    @InjectMocks
    CommentsServiceImpl mockService;

    @Test
    public void getAllShould_ReturnListOfComments() {
        // Arrange
        Mockito.when(mockRepository.findAllByPostOrderByTimestampDesc(any(Post.class)))
                .thenReturn(Arrays.asList(createComment()));

        // Act
        Collection<Comment> list = mockService.getAll(createPostPublic());

        // Assert
        Assert.assertEquals(1, list.size());
    }

    @Test
    public void getAllShould_ReturnEmptyListOfComments() {
        // Arrange
        Mockito.when(mockRepository.findAllByPostOrderByTimestampDesc(any(Post.class)))
                .thenReturn(Collections.emptyList());

        // Act
        Collection<Comment> list = mockService.getAll(createPostPublic());

        // Assert
        Assert.assertEquals(0, list.size());
    }

    @Test
    public void getLatest5Should_ReturnListOfComments() {
        // Arrange
        Mockito.when(mockRepository.findLatest5Comments(anyLong()))
                .thenReturn(Arrays.asList(createComment()));

        // Act
        Collection<Comment> list = mockService.getLatest5(createPostPublic());

        // Assert
        Assert.assertEquals(1, list.size());
    }

    @Test
    public void getLatest5Should_ReturnEmptyListOfComments() {
        // Arrange
        Mockito.when(mockRepository.findLatest5Comments(anyLong()))
                .thenReturn(Collections.emptyList());

        // Act
        Collection<Comment> list = mockService.getLatest5(createPostPublic());

        // Assert
        Assert.assertEquals(0, list.size());
    }

    @Test
    public void getCommentByIdShould_ReturnComment_WhenCommentExists() {
        // Arrange
        Comment expected = createComment();

        Mockito.when(mockRepository.findById(anyLong()))
                .thenReturn(Optional.of(expected));

        // Act
        Comment actual = mockService.getCommentById(anyLong());

        // Assert
        Assert.assertSame(expected, actual);
    }

    @Test
    public void getCommentByIdShould_Throw_WhenCommentNotExists() {
        // Arrange, Act, Assert
        Assert.assertThrows(EntityNotFoundException.class,
                () -> mockService.getCommentById(anyLong()));
    }

    @Test
    public void getCommentByIdShould_CallRepository() {
        // Arrange
        Comment expected = createComment();

        Mockito.when(mockRepository.findById(anyLong()))
                .thenReturn(Optional.of(expected));

        // Act
        mockService.getCommentById(anyLong());

        // Assert
        Mockito.verify(mockRepository,
                times(1)).findById(anyLong());
    }

    @Test
    public void saveCommentShould_ReturnComment() {
        // Arrange
        Comment comment = createComment();

        Mockito.when(mockRepository.save(any(Comment.class)))
                .thenReturn(comment);

        // Act
        Comment actual = mockService.saveComment(comment);

        // Assert
        Assert.assertSame(comment, actual);
    }

    @Test
    public void deleteCommentShould_CallRepository() {
        // Arrange
        Comment comment = createComment();

        // Act
        mockService.deleteComment(comment);

        // Assert
        Mockito.verify(mockRepository, times(1)).delete(comment);
    }
}
