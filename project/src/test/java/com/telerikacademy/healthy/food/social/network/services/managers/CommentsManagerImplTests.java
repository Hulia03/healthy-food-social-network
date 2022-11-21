package com.telerikacademy.healthy.food.social.network.services.managers;

import com.telerikacademy.healthy.food.social.network.models.Comment;
import com.telerikacademy.healthy.food.social.network.models.Post;
import com.telerikacademy.healthy.food.social.network.models.UserDetails;
import com.telerikacademy.healthy.food.social.network.services.CommentsServiceImpl;
import com.telerikacademy.healthy.food.social.network.services.UserDetailsServiceImpl;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.access.AccessDeniedException;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import static com.telerikacademy.healthy.food.social.network.Factory.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.times;

@RunWith(MockitoJUnitRunner.class)
public class CommentsManagerImplTests {
    @Mock
    CommentsServiceImpl mockService;

    @Mock
    PostsManagerImpl mockPostsManager;

    @Mock
    UserDetailsServiceImpl mockUserDetailsService;

    @InjectMocks
    CommentsManagerImpl mockManager;

    @Test
    public void getAllShould_ReturnListOfComments() {
        // Arrange
        UserDetails logged = createUserDetails();
        Post post = createPostPublic();
        Mockito.when(mockPostsManager.getPostById(post.getId(), logged))
                .thenReturn(post);

        Mockito.when(mockService.getAll(post))
                .thenReturn(Arrays.asList(createComment()));

        // Act
        Collection<Comment> list = mockManager.getAll(post.getId(), logged);

        // Assert
        Assert.assertEquals(1, list.size());
    }

    @Test
    public void getAllShould_ReturnEmptyListOfComments() {
        // Arrange
        UserDetails logged = createUserDetails();
        Post post = createPostPublic();
        Mockito.when(mockPostsManager.getPostById(post.getId(), logged))
                .thenReturn(post);

        Mockito.when(mockService.getAll(post))
                .thenReturn(Collections.emptyList());

        // Act
        Collection<Comment> list = mockManager.getAll(post.getId(), logged);

        // Assert
        Assert.assertEquals(0, list.size());
    }

    @Test
    public void getCommentByIdShould_ReturnComment_WhenCommentExists() {
        // Arrange
        UserDetails logged = createUserDetails();
        Comment comment = createComment();

        Mockito.when(mockService.getCommentById(anyLong()))
                .thenReturn(comment);

        Mockito.when(mockPostsManager.checkAccessToPost(comment.getPost(), logged))
                .thenReturn(true);

        // Act
        Comment actual = mockManager.getCommentById(comment.getId(), logged);

        // Assert
        Assert.assertSame(comment, actual);
    }

    @Test
    public void getCommentByIdShould_Throw_WhenAccessDeniedNotExists() {
        // Arrange
        UserDetails logged = createUserDetails();
        Comment comment = createComment();

        Mockito.when(mockService.getCommentById(anyLong()))
                .thenReturn(comment);

        Mockito.when(mockPostsManager.checkAccessToPost(comment.getPost(), logged))
                .thenReturn(false);

        // Act, Assert
        Assert.assertThrows(AccessDeniedException.class,
                () -> mockManager.getCommentById(1, logged));
    }

    @Test
    public void getPublicCommentByIdShould_ReturnComment_WhenCommentExists() {
        // Arrange
        Comment comment = createComment();

        Mockito.when(mockService.getCommentById(anyLong()))
                .thenReturn(comment);

        Mockito.when(mockPostsManager.checkPostPublic(1))
                .thenReturn(true);

        // Act
        Comment actual = mockManager.getPublicCommentById(comment.getId());

        // Assert
        Assert.assertSame(comment, actual);
    }

    @Test
    public void getPublicCommentByIdShould_Throw_WhenAccessDeniedNotExists() {
        // Arrange
        Comment comment = createComment();

        Mockito.when(mockService.getCommentById(anyLong()))
                .thenReturn(comment);

        Mockito.when(mockPostsManager.checkPostPublic(1))
                .thenReturn(false);

        // Act, Assert
        Assert.assertThrows(AccessDeniedException.class,
                () -> mockManager.getPublicCommentById(1));
    }

    @Test
    public void createCommentShould_ReturnComment() {
        // Arrange
        UserDetails logged = createUserDetails();
        Mockito.when(mockPostsManager.getPostById(1, logged))
                .thenReturn(createPostPublic());

        Comment comment = createComment();
        Mockito.when(mockService.saveComment(any(Comment.class))).thenReturn(comment);

        // Act
        Comment actual = mockManager.createComment(1, comment, logged);

        // Assert
        Assert.assertSame(comment, actual);
    }

    @Test
    public void updateCommentShould_ReturnComment() {
        // Arrange
        UserDetails logged = createUserDetails();
        Comment comment = createComment();
        Mockito.when(mockService.saveComment(any(Comment.class))).thenReturn(comment);

        // Act
        Comment actual = mockManager.updateComment(comment, logged);

        // Assert
        Assert.assertSame(comment, actual);
    }

    @Test
    public void updateCommentsShould_Throw_WhenUserIsNotAuthorize() {
        // Arrange
        UserDetails logged = createSender();
        Comment comment = createComment();

        Mockito.when(mockUserDetailsService.adminPermission(anyLong()))
                .thenReturn(false);

        // Act, Assert
        Assert.assertThrows(AccessDeniedException.class,
                () -> mockManager.updateComment(comment, logged));
    }

    @Test
    public void deleteCommentShould_CallRepository() {
        // Arrange
        UserDetails logged = createUserDetails();
        Comment comment = createComment();

        Mockito.doNothing().when(mockService).deleteComment(comment);

        // Act
        mockManager.deleteComment(comment, logged);

        // Assert
        Mockito.verify(mockService,
                times(1)).deleteComment(comment);
    }

    @Test
    public void deleteCommentsShould_Throw_WhenUserIsNotAuthorize() {
        // Arrange
        UserDetails logged = createSender();
        Comment comment = createComment();

        Mockito.when(mockUserDetailsService.adminPermission(anyLong()))
                .thenReturn(false);

        // Act, Assert
        Assert.assertThrows(AccessDeniedException.class,
                () -> mockManager.deleteComment(comment, logged));
    }

    @Test
    public void getCommentsLikesShould_ReturnZeroLikes_WhenNoLikes() {
        // Arrange
        UserDetails logged = createUserDetails();
        Comment comment = createComment();

        Mockito.when(mockPostsManager.checkAccessToPost(comment.getPost(), logged))
                .thenReturn(true);

        Mockito.when(mockService.getCommentById(anyLong()))
                .thenReturn(comment);

        // Act
        long actual = mockManager.getCommentsLikes(comment.getId(), logged);

        // Assert
        Assert.assertEquals(0, actual);
    }

    @Test
    public void getCommentsLikesShould_ReturnCountOfLikes() {
        // Arrange
        UserDetails logged = createUserDetails();
        Comment comment = createCommentWithLikes();

        Mockito.when(mockPostsManager.checkAccessToPost(comment.getPost(), logged))
                .thenReturn(true);

        Mockito.when(mockService.getCommentById(anyLong()))
                .thenReturn(comment);

        // Act
        long actual = mockManager.getCommentsLikes(comment.getId(), logged);

        // Assert
        Assert.assertEquals(comment.getLikedUsers().size(), actual);
    }

    @Test
    public void likeCommentShould_ReturnCommentWithOneLessLike_WhenUserAlreadyLikedIt() {
        // Arrange
        UserDetails logged = createUserDetails();
        Comment comment = createCommentWithLikes();

        Mockito.when(mockPostsManager.checkAccessToPost(comment.getPost(), logged))
                .thenReturn(true);

        Mockito.when(mockService.getCommentById(anyLong()))
                .thenReturn(comment);

        Mockito.when(mockService.saveComment(comment))
                .thenReturn(comment);

        // Act
        long expected = comment.getLikedUsers().size() - 1;
        long actual = mockManager.likeComment(comment.getId(), logged).getLikedUsers().size();

        // Assert
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void likeCommentShould_ReturnCommentWithOneMoreLike() {
        // Arrange
        UserDetails logged = createSender();
        Comment comment = createCommentWithLikes();

        Mockito.when(mockPostsManager.checkAccessToPost(comment.getPost(), logged))
                .thenReturn(true);

        Mockito.when(mockService.getCommentById(anyLong()))
                .thenReturn(comment);

        Mockito.when(mockService.saveComment(comment))
                .thenReturn(comment);

        // Act
        long expected = comment.getLikedUsers().size() + 1;
        long actual = mockManager.likeComment(comment.getId(), logged).getLikedUsers().size();

        // Assert
        Assert.assertEquals(expected, actual);
    }
}
