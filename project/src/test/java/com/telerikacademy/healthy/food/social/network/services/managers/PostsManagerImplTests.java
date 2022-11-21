package com.telerikacademy.healthy.food.social.network.services.managers;

import com.telerikacademy.healthy.food.social.network.models.Post;
import com.telerikacademy.healthy.food.social.network.models.UserDetails;
import com.telerikacademy.healthy.food.social.network.services.ConnectionsServiceImpl;
import com.telerikacademy.healthy.food.social.network.services.PostsServiceImpl;
import com.telerikacademy.healthy.food.social.network.services.UserDetailsServiceImpl;
import com.telerikacademy.healthy.food.social.network.services.VisibilitiesServiceImpl;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;

import java.util.Arrays;
import java.util.Collection;

import static com.telerikacademy.healthy.food.social.network.Factory.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;

@RunWith(MockitoJUnitRunner.class)
public class PostsManagerImplTests {
    @Mock
    Pageable pageable;

    @Mock
    PostsServiceImpl mockPostsService;

    @Mock
    UserDetailsServiceImpl mockUserDetailsService;

    @Mock
    ConnectionsServiceImpl mockConnectionsService;

    @Mock
    VisibilitiesServiceImpl mockVisibilitiesService;

    @InjectMocks
    PostsManagerImpl mockManager;

    @Test
    public void getAllUserPostsShould_ReturnListOfAllPublicPosts_WhenUsersAreNotConnected() {
        // Arrange
        UserDetails userDetails = createUserDetails();
        UserDetails logged = createSender();

        Mockito.when(mockConnectionsService.checkUsersConnected(userDetails, logged))
                .thenReturn(false);

        Mockito.when(mockPostsService.getAllPublicUserPosts(userDetails, pageable))
                .thenReturn(Arrays.asList(createPostPublic()));

        // Act
        Collection<Post> list = mockManager.getAllUserPosts(userDetails, logged, pageable);

        // Assert
        Assert.assertEquals(1, list.size());
    }

    @Test
    public void getAllUserPostsShould_ReturnListOfAllUserPosts_WhenUsersIsAdmin() {
        // Arrange
        UserDetails userDetails = createUserDetails();
        UserDetails logged = createSender();

        Mockito.when(mockConnectionsService.checkUsersConnected(userDetails, logged))
                .thenReturn(false);

        Mockito.when(mockUserDetailsService.adminPermission(logged.getId()))
                .thenReturn(true);

        Mockito.when(mockPostsService.getAllUserPosts(userDetails, pageable))
                .thenReturn(Arrays.asList(createPostPublic()));

        // Act
        Collection<Post> list = mockManager.getAllUserPosts(userDetails, logged, pageable);

        // Assert
        Assert.assertEquals(1, list.size());
        Mockito.verify(mockPostsService,
                times(0)).getAllPublicUserPosts(userDetails, pageable);
    }

    @Test
    public void getPublicPostByIdShould_Throw_WhenPostIsNotPublic() {
        // Arrange
        Post post = createPostPublic();
        Mockito.when(mockPostsService.getPostById(anyLong()))
                .thenReturn(post);

        Mockito.when(mockVisibilitiesService.checkPostPublic(post))
                .thenReturn(false);

        // Act, Assert
        Assert.assertThrows(AccessDeniedException.class,
                () -> mockManager.getPublicPostById(anyLong()));
    }

    @Test
    public void getPublicPostByIdShould_ReturnPost() {
        // Arrange
        Post expected = createPostPublic();
        Mockito.when(mockPostsService.getPostById(anyLong()))
                .thenReturn(expected);

        Mockito.when(mockVisibilitiesService.checkPostPublic(expected))
                .thenReturn(true);

        // Act
        Post actual = mockManager.getPublicPostById(anyLong());

        // Assert
        Assert.assertSame(expected, actual);
    }

    @Test
    public void getPostByIdShould_ReturnPost_WhenPostIsPublic() {
        // Arrange
        UserDetails logged = createUserDetails();
        Post expected = createPostPublic();

        Mockito.when(mockPostsService.getPostById(anyLong()))
                .thenReturn(expected);

        Mockito.when(mockVisibilitiesService.checkPostPublic(expected))
                .thenReturn(true);

        // Act
        Post actual = mockManager.getPostById(expected.getId(), logged);

        // Assert
        Assert.assertSame(expected, actual);
    }

    @Test
    public void getPostByIdShould_ReturnPost_WhenUserIsAdmin() {
        // Arrange
        UserDetails logged = createUserDetails();
        Post expected = createPostPublic();

        Mockito.when(mockPostsService.getPostById(anyLong()))
                .thenReturn(expected);

        Mockito.when(mockVisibilitiesService.checkPostPublic(expected))
                .thenReturn(false);

        Mockito.when(mockUserDetailsService.adminPermission(anyLong()))
                .thenReturn(true);

        // Act
        Post actual = mockManager.getPostById(expected.getId(), logged);

        // Assert
        Assert.assertSame(expected, actual);
    }

    @Test
    public void getPostByIdShould_Throw_WhenUserIsNotAuthorize() {
        // Arrange
        UserDetails logged = createUserDetails();
        Post expected = createPostPublic();

        Mockito.when(mockPostsService.getPostById(anyLong()))
                .thenReturn(expected);

        Mockito.when(mockVisibilitiesService.checkPostPublic(expected))
                .thenReturn(false);

        // Act, Assert
        Assert.assertThrows(AccessDeniedException.class,
                () -> mockManager.getPostById(expected.getId(), logged));
    }

    @Test
    public void updatePostShould_ReturnPost() {
        // Arrange
        UserDetails logged = createUserDetails();
        Post expected = createPostPublic();

        Mockito.when(mockPostsService.savePost(any(Post.class)))
                .thenReturn(expected);

        // Act
        Post actual = mockManager.updatePost(expected, logged);

        // Assert
        Assert.assertSame(expected, actual);
    }

    @Test
    public void updatePostShould_Throw_WhenUserIsNotAuthorize() {
        // Arrange
        UserDetails logged = createSender();
        Post post = createPostPublic();

        Mockito.when(mockUserDetailsService.adminPermission(anyLong()))
                .thenReturn(false);

        // Act, Assert
        Assert.assertThrows(AccessDeniedException.class,
                () -> mockManager.updatePost(post, logged));
    }

    @Test
    public void deletePostShould_CallRepository() {
        // Arrange
        UserDetails logged = createUserDetails();
        Post post = createPostPublic();

        Mockito.doNothing().when(mockPostsService).deletePost(post);

        // Act
        mockManager.deletePost(post, logged);

        // Assert
        Mockito.verify(mockPostsService,
                times(1)).deletePost(post);
    }

    @Test
    public void deletePostShould_Throw_WhenUserIsNotAuthorize() {
        // Arrange
        UserDetails logged = createSender();
        Post post = createPostPublic();

        Mockito.when(mockUserDetailsService.adminPermission(anyLong()))
                .thenReturn(false);

        // Act, Assert
        Assert.assertThrows(AccessDeniedException.class,
                () -> mockManager.deletePost(post, logged));
    }

    @Test
    public void getPostsLikesShould_ReturnZeroLikes_WhenNoLikes() {
        // Arrange
        UserDetails logged = createUserDetails();
        Post post = createPostPublic();

        Mockito.when(mockPostsService.getPostById(anyLong()))
                .thenReturn(post);

        Mockito.when(mockVisibilitiesService.checkPostPublic(post))
                .thenReturn(true);

        // Act
        long actual = mockManager.getPostsLikes(post.getId(), logged);

        // Assert
        Assert.assertEquals(post.getLikedUsers().size(), actual);
    }

    @Test
    public void likePostShould_ReturnPostWithOneLessLike_WhenUserAlreadyLikedIt() {
        // Arrange
        UserDetails logged = createUserDetails();
        Post post = createPostWithLikes();

        Mockito.when(mockPostsService.getPostById(anyLong()))
                .thenReturn(post);

        Mockito.when(mockVisibilitiesService.checkPostPublic(post))
                .thenReturn(true);

        Mockito.when(mockPostsService.savePost(post))
                .thenReturn(post);

        // Act
        long expected = post.getLikedUsers().size() - 1;
        long actual = mockManager.likePost(post.getId(), logged).getLikedUsers().size();

        // Assert
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void likePostShould_ReturnPostWithOneMoreLike() {
        // Arrange
        UserDetails logged = createUserDetails();
        Post post = createPostPublic();

        Mockito.when(mockPostsService.getPostById(anyLong()))
                .thenReturn(post);

        Mockito.when(mockVisibilitiesService.checkPostPublic(post))
                .thenReturn(true);

        Mockito.when(mockPostsService.savePost(post))
                .thenReturn(post);

        // Act
        long expected = post.getLikedUsers().size() + 1;
        long actual = mockManager.likePost(post.getId(), logged).getLikedUsers().size();

        // Assert
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void checkPostPublicShould_ReturnTrue_WhenPostIsPublic() {
        // Arrange
        Post post = createPostPublic();

        Mockito.when(mockPostsService.getPostById(anyLong()))
                .thenReturn(post);

        Mockito.when(mockVisibilitiesService.checkPostPublic(post))
                .thenReturn(true);

        // Act
        boolean actual = mockManager.checkPostPublic(post.getId());

        // Assert
        Assert.assertTrue(actual);
    }
}
