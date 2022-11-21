package com.telerikacademy.healthy.food.social.network.services;

import com.telerikacademy.healthy.food.social.network.exceptions.EntityNotFoundException;
import com.telerikacademy.healthy.food.social.network.models.Category;
import com.telerikacademy.healthy.food.social.network.models.Post;
import com.telerikacademy.healthy.food.social.network.models.UserDetails;
import com.telerikacademy.healthy.food.social.network.models.Visibility;
import com.telerikacademy.healthy.food.social.network.repositories.contracts.CategoriesRepository;
import com.telerikacademy.healthy.food.social.network.repositories.contracts.PostsRepository;
import com.telerikacademy.healthy.food.social.network.repositories.contracts.VisibilitiesRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

import static com.telerikacademy.healthy.food.social.network.Factory.*;
import static com.telerikacademy.healthy.food.social.network.utils.GlobalConstants.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.times;

@RunWith(MockitoJUnitRunner.class)
public class PostsServiceImplTests {
    @Mock
    PostsRepository mockRepository;

    @Mock
    VisibilitiesRepository mockVisibilitiesRepository;

    @Mock
    CategoriesRepository mockCategoriesRepository;

    @Mock
    Pageable pageable;

    @InjectMocks
    PostsServiceImpl mockService;

    @Test
    public void getAllShould_ReturnListOfPostsByTitle() {
        // Arrange
        Page<Post> posts = new PageImpl(Arrays.asList(createPostConnected()));
        Mockito.when(mockRepository.findAllByTitleLikeOrderByTimestampDesc("%" + TITLE + "%", pageable))
                .thenReturn(posts);

        // Act
        Collection<Post> list = mockService.getAll(TITLE, 0, "", pageable);

        // Assert
        Assert.assertEquals(1, list.size());
    }

    @Test
    public void getAllShould_ReturnListOfPostsByCategory() {
        // Arrange
        Category category = createCategoryVegan();
        Mockito.when(mockCategoriesRepository.findById(anyInt()))
                .thenReturn(Optional.of(category));

        Page<Post> posts = new PageImpl(Arrays.asList(createPostConnected()));
        Mockito.when(mockRepository.findAllByCategoriesContainsOrderByTimestampDesc(category, pageable))
                .thenReturn(posts);

        // Act
        Collection<Post> list = mockService.getAll("", 2, "", pageable);

        // Assert
        Assert.assertEquals(1, list.size());
    }

    @Test
    public void getAllShould_Throw_WhenCategoryNotExist() {
        // Arrange, Act, Assert
        Assert.assertThrows(EntityNotFoundException.class,
                () -> mockService.getAll("", 2, "", pageable));
    }

    @Test
    public void getAllShould_ReturnListOfPostsSortByLikes() {
        // Arrange
        Page<Post> posts = new PageImpl(Arrays.asList(createPostConnected()));
        Mockito.when(mockRepository.findAllOrderByLikes(pageable))
                .thenReturn(posts);

        // Act
        Collection<Post> list = mockService.getAll("", 0, LIKES, pageable);

        // Assert
        Assert.assertEquals(1, list.size());
    }

    @Test
    public void getAllShould_ReturnListOfPostsSortByComments() {
        // Arrange
        Page<Post> posts = new PageImpl(Arrays.asList(createPostConnected()));
        Mockito.when(mockRepository.findAllOrderByCommentsDesc(pageable))
                .thenReturn(posts);

        // Act
        Collection<Post> list = mockService.getAll("", 0, COMMENTS, pageable);

        // Assert
        Assert.assertEquals(1, list.size());
    }

    @Test
    public void getAllShould_ReturnListOfPosts() {
        // Arrange
        Page<Post> posts = new PageImpl(Arrays.asList(createPostConnected()));
        Mockito.when(mockRepository.findAllByOrderByTimestampDesc(pageable))
                .thenReturn(posts);

        // Act
        Collection<Post> list = mockService.getAll("", 0, "", pageable);

        // Assert
        Assert.assertEquals(1, list.size());
    }

    @Test
    public void getAllShould_ReturnEmptyListOfPostsByTitle() {
        // Arrange
        Page<Post> posts = Mockito.mock(Page.class);
        Mockito.when(mockRepository.findAllByTitleLikeOrderByTimestampDesc("%" + TITLE + "%", pageable))
                .thenReturn(posts);

        // Act
        Collection<Post> list = mockService.getAll(TITLE, 0, "", pageable);

        // Assert
        Assert.assertEquals(0, list.size());
    }

    @Test
    public void getAllShould_ReturnEmptyListOfPostsByCategory() {
        // Arrange
        Category category = createCategoryVegan();
        Mockito.when(mockCategoriesRepository.findById(anyInt()))
                .thenReturn(Optional.of(category));

        Page<Post> posts = Mockito.mock(Page.class);
        Mockito.when(mockRepository.findAllByCategoriesContainsOrderByTimestampDesc(category, pageable))
                .thenReturn(posts);

        // Act
        Collection<Post> list = mockService.getAll("", 2, "", pageable);

        // Assert
        Assert.assertEquals(0, list.size());
    }

    @Test
    public void getAllShould_ReturnEmptyListOfPostsSortByLikes() {
        // Arrange
        Page<Post> posts = Mockito.mock(Page.class);
        Mockito.when(mockRepository.findAllOrderByLikes(pageable))
                .thenReturn(posts);

        // Act
        Collection<Post> list = mockService.getAll("", 0, LIKES, pageable);

        // Assert
        Assert.assertEquals(0, list.size());
    }

    @Test
    public void getAllShould_ReturnEmptyListOfPostsSortByComments() {
        // Arrange
        Page<Post> posts = Mockito.mock(Page.class);
        Mockito.when(mockRepository.findAllOrderByCommentsDesc(pageable))
                .thenReturn(posts);

        // Act
        Collection<Post> list = mockService.getAll("", 0, COMMENTS, pageable);

        // Assert
        Assert.assertEquals(0, list.size());
    }

    @Test
    public void getAllShould_ReturnEmptyListOfPosts() {
        // Arrange
        Page<Post> posts = Mockito.mock(Page.class);
        Mockito.when(mockRepository.findAllByOrderByTimestampDesc(pageable))
                .thenReturn(posts);

        // Act
        Collection<Post> list = mockService.getAll("", 0, "", pageable);

        // Assert
        Assert.assertEquals(0, list.size());
    }

    @Test
    public void getAllPublicShould_ReturnListOfPostsByTitle() {
        // Arrange
        Visibility visibility = createVisibilityPublic();
        Mockito.when(mockVisibilitiesRepository.findByType(anyString()))
                .thenReturn(visibility);

        Page<Post> posts = new PageImpl(Arrays.asList(createPostConnected()));
        Mockito.when(mockRepository.findAllByVisibilityAndTitleLikeOrderByTimestampDesc(visibility, "%" + TITLE + "%", pageable))
                .thenReturn(posts);

        // Act
        Collection<Post> list = mockService.getAllPublic(TITLE, 0, "", pageable);

        // Assert
        Assert.assertEquals(1, list.size());
    }

    @Test
    public void getAllPublicShould_ReturnListOfPostsByCategory() {
        // Arrange
        Visibility visibility = createVisibilityPublic();
        Mockito.when(mockVisibilitiesRepository.findByType(anyString()))
                .thenReturn(visibility);

        Category category = createCategoryVegan();
        Mockito.when(mockCategoriesRepository.findById(anyInt()))
                .thenReturn(Optional.of(category));

        Page<Post> posts = new PageImpl(Arrays.asList(createPostConnected()));
        Mockito.when(mockRepository.findAllByVisibilityAndCategoriesContainsOrderByTimestampDesc(visibility, category, pageable))
                .thenReturn(posts);

        // Act
        Collection<Post> list = mockService.getAllPublic("", 2, "", pageable);

        // Assert
        Assert.assertEquals(1, list.size());
    }

    @Test
    public void getAllPublicShould_Throw_WhenCategoryNotExist() {
        // Arrange, Act, Assert
        Assert.assertThrows(EntityNotFoundException.class,
                () -> mockService.getAllPublic("", 2, "", pageable));
    }

    @Test
    public void getAllPublicShould_ReturnListOfPostsSortByLikes() {
        // Arrange
        Page<Post> posts = new PageImpl(Arrays.asList(createPostConnected()));
        Mockito.when(mockRepository.findAllPublicOrderByLikes(pageable))
                .thenReturn(posts);

        // Act
        Collection<Post> list = mockService.getAllPublic("", 0, LIKES, pageable);

        // Assert
        Assert.assertEquals(1, list.size());
    }

    @Test
    public void getAllPublicShould_ReturnListOfPostsSortByComments() {
        // Arrange
        Page<Post> posts = new PageImpl(Arrays.asList(createPostConnected()));
        Mockito.when(mockRepository.findAllPublicOrderByCommentsDesc(pageable))
                .thenReturn(posts);

        // Act
        Collection<Post> list = mockService.getAllPublic("", 0, COMMENTS, pageable);

        // Assert
        Assert.assertEquals(1, list.size());
    }

    @Test
    public void getAllPublicShould_ReturnListOfPosts() {
        // Arrange
        Visibility visibility = createVisibilityPublic();
        Mockito.when(mockVisibilitiesRepository.findByType(anyString()))
                .thenReturn(visibility);

        Page<Post> posts = new PageImpl(Arrays.asList(createPostConnected()));
        Mockito.when(mockRepository.findAllByVisibilityOrderByTimestampDesc(visibility, pageable))
                .thenReturn(posts);

        // Act
        Collection<Post> list = mockService.getAllPublic("", 0, "", pageable);

        // Assert
        Assert.assertEquals(1, list.size());
    }

    @Test
    public void getAllPublicShould_ReturnEmptyListOfPostsByTitle() {
        // Arrange
        Visibility visibility = createVisibilityPublic();
        Mockito.when(mockVisibilitiesRepository.findByType(anyString()))
                .thenReturn(visibility);

        Page<Post> posts = Mockito.mock(Page.class);
        Mockito.when(mockRepository.findAllByVisibilityAndTitleLikeOrderByTimestampDesc(visibility, "%" + TITLE + "%", pageable))
                .thenReturn(posts);

        // Act
        Collection<Post> list = mockService.getAllPublic(TITLE, 0, "", pageable);

        // Assert
        Assert.assertEquals(0, list.size());
    }

    @Test
    public void getAllPublicShould_ReturnEmptyListOfPostsByCategory() {
        // Arrange
        Visibility visibility = createVisibilityPublic();
        Mockito.when(mockVisibilitiesRepository.findByType(anyString()))
                .thenReturn(visibility);

        Category category = createCategoryVegan();
        Mockito.when(mockCategoriesRepository.findById(anyInt()))
                .thenReturn(Optional.of(category));

        Page<Post> posts = Mockito.mock(Page.class);
        Mockito.when(mockRepository.findAllByVisibilityAndCategoriesContainsOrderByTimestampDesc(visibility, category, pageable))
                .thenReturn(posts);

        // Act
        Collection<Post> list = mockService.getAllPublic("", 2, "", pageable);

        // Assert
        Assert.assertEquals(0, list.size());
    }

    @Test
    public void getAllPublicShould_ReturnEmptyListOfPostsSortByLikes() {
        // Arrange
        Page<Post> posts = Mockito.mock(Page.class);
        Mockito.when(mockRepository.findAllPublicOrderByLikes(pageable))
                .thenReturn(posts);

        // Act
        Collection<Post> list = mockService.getAllPublic("", 0, LIKES, pageable);

        // Assert
        Assert.assertEquals(0, list.size());
    }

    @Test
    public void getAllPublicShould_ReturnEmptyListOfPostsSortByComments() {
        // Arrange
        Page<Post> posts = Mockito.mock(Page.class);
        Mockito.when(mockRepository.findAllPublicOrderByCommentsDesc(pageable))
                .thenReturn(posts);

        // Act
        Collection<Post> list = mockService.getAllPublic("", 0, COMMENTS, pageable);

        // Assert
        Assert.assertEquals(0, list.size());
    }

    @Test
    public void getAllPublicShould_ReturnEmptyListOfPosts() {
        // Arrange
        Visibility visibility = createVisibilityPublic();
        Mockito.when(mockVisibilitiesRepository.findByType(anyString()))
                .thenReturn(visibility);

        Page<Post> posts = Mockito.mock(Page.class);
        Mockito.when(mockRepository.findAllByVisibilityOrderByTimestampDesc(visibility, pageable))
                .thenReturn(posts);

        // Act
        Collection<Post> list = mockService.getAllPublic("", 0, "", pageable);

        // Assert
        Assert.assertEquals(0, list.size());
    }

    @Test
    public void getAllConnectedUsersPostsShould_ReturnListOfPostsByTitle() {
        // Arrange
        UserDetails logged = createUserDetails();
        Page<Post> posts = new PageImpl(Arrays.asList(createPostConnected()));
        Mockito.when(mockRepository.findAllConnectedUsersPostsByTitle(TITLE, logged.getId(), pageable))
                .thenReturn(posts);

        // Act
        Collection<Post> list = mockService.getAllConnectedUsersPost(TITLE, 0, "", logged, pageable);

        // Assert
        Assert.assertEquals(1, list.size());
    }

    @Test
    public void getAllConnectedUsersPostsShould_ReturnListOfPostsByCategory() {
        // Arrange
        UserDetails logged = createUserDetails();

        Page<Post> posts = new PageImpl(Arrays.asList(createPostConnected()));
        Mockito.when(mockRepository.findAllConnectedUsersPostsByCategory(2, logged.getId(), pageable))
                .thenReturn(posts);

        // Act
        Collection<Post> list = mockService.getAllConnectedUsersPost("", 2, "", logged, pageable);

        // Assert
        Assert.assertEquals(1, list.size());
    }

    @Test
    public void getAllConnectedUsersPostsShould_ReturnListOfPostsSortByLikes() {
        // Arrange
        UserDetails logged = createUserDetails();
        Page<Post> posts = new PageImpl(Arrays.asList(createPostConnected()));
        Mockito.when(mockRepository.findAllConnectedUsersPostsOrderByLikes(logged.getId(), pageable))
                .thenReturn(posts);

        // Act
        Collection<Post> list = mockService.getAllConnectedUsersPost("", 0, LIKES, logged, pageable);

        // Assert
        Assert.assertEquals(1, list.size());
    }

    @Test
    public void getAllConnectedUsersPostsShould_ReturnListOfPostsSortByComments() {
        // Arrange
        UserDetails logged = createUserDetails();
        Page<Post> posts = new PageImpl(Arrays.asList(createPostConnected()));
        Mockito.when(mockRepository.findAllConnectedUsersPostsOrderByComments(logged.getId(), pageable))
                .thenReturn(posts);

        // Act
        Collection<Post> list = mockService.getAllConnectedUsersPost("", 0, COMMENTS, logged, pageable);

        // Assert
        Assert.assertEquals(1, list.size());
    }

    @Test
    public void getAllConnectedUsersPostsShould_ReturnListOfPosts() {
        // Arrange
        UserDetails logged = createUserDetails();
        Page<Post> posts = new PageImpl(Arrays.asList(createPostConnected()));
        Mockito.when(mockRepository.findAllConnectedUsersPostsOrderByDates(logged.getId(), pageable))
                .thenReturn(posts);

        // Act
        Collection<Post> list = mockService.getAllConnectedUsersPost("", 0, "", logged, pageable);

        // Assert
        Assert.assertEquals(1, list.size());
    }

    @Test
    public void getAllConnectedUsersPostsShould_ReturnEmptyListOfPostsByTitle() {
        // Arrange
        UserDetails logged = createUserDetails();
        Page<Post> posts = Mockito.mock(Page.class);
        Mockito.when(mockRepository.findAllConnectedUsersPostsByTitle(TITLE, logged.getId(), pageable))
                .thenReturn(posts);

        // Act
        Collection<Post> list = mockService.getAllConnectedUsersPost(TITLE, 0, "", logged, pageable);

        // Assert
        Assert.assertEquals(0, list.size());
    }

    @Test
    public void getAllConnectedUsersPostsShould_ReturnEmptyListOfPostsByCategory() {
        // Arrange
        UserDetails logged = createUserDetails();
        Page<Post> posts = Mockito.mock(Page.class);
        Mockito.when(mockRepository.findAllConnectedUsersPostsByCategory(2, logged.getId(), pageable))
                .thenReturn(posts);

        // Act
        Collection<Post> list = mockService.getAllConnectedUsersPost("", 2, "", logged, pageable);

        // Assert
        Assert.assertEquals(0, list.size());
    }

    @Test
    public void getAllConnectedUsersPostsShould_ReturnEmptyListOfPostsSortByLikes() {
        // Arrange
        UserDetails logged = createUserDetails();
        Page<Post> posts = Mockito.mock(Page.class);
        Mockito.when(mockRepository.findAllConnectedUsersPostsOrderByLikes(logged.getId(), pageable))
                .thenReturn(posts);

        // Act
        Collection<Post> list = mockService.getAllConnectedUsersPost("", 0, LIKES, logged, pageable);

        // Assert
        Assert.assertEquals(0, list.size());
    }

    @Test
    public void getAllConnectedUsersPostsShould_ReturnEmptyListOfPostsSortByComments() {
        // Arrange
        UserDetails logged = createUserDetails();
        Page<Post> posts = Mockito.mock(Page.class);
        Mockito.when(mockRepository.findAllConnectedUsersPostsOrderByComments(logged.getId(), pageable))
                .thenReturn(posts);

        // Act
        Collection<Post> list = mockService.getAllConnectedUsersPost("", 0, COMMENTS, logged, pageable);

        // Assert
        Assert.assertEquals(0, list.size());
    }

    @Test
    public void getAllConnectedUsersPostsShould_ReturnEmptyListOfPosts() {
        // Arrange
        UserDetails logged = createUserDetails();
        Page<Post> posts = Mockito.mock(Page.class);
        Mockito.when(mockRepository.findAllConnectedUsersPostsOrderByDates(logged.getId(), pageable))
                .thenReturn(posts);

        // Act
        Collection<Post> list = mockService.getAllConnectedUsersPost("", 0, "", logged, pageable);

        // Assert
        Assert.assertEquals(0, list.size());
    }

    @Test
    public void getAllUserPostsShould_ReturnListOfPosts() {
        // Arrange
        UserDetails logged = createUserDetails();
        Page<Post> posts = new PageImpl(Arrays.asList(createPostConnected()));
        Mockito.when(mockRepository.findAllByCreatorOrderByTimestampDesc(logged, pageable))
                .thenReturn(posts);

        // Act
        Collection<Post> list = mockService.getAllUserPosts(logged, pageable);

        // Assert
        Assert.assertEquals(1, list.size());
    }

    @Test
    public void getAllUserPostsShould_ReturnEmptyListOfPostsByTitle() {
        // Arrange
        UserDetails logged = createUserDetails();
        Page<Post> posts = Mockito.mock(Page.class);
        Mockito.when(mockRepository.findAllByCreatorOrderByTimestampDesc(logged, pageable))
                .thenReturn(posts);

        // Act
        Collection<Post> list = mockService.getAllUserPosts(logged, pageable);

        // Assert
        Assert.assertEquals(0, list.size());
    }

    @Test
    public void getAllPublicUserPostsShould_ReturnListOfPosts() {
        // Arrange
        UserDetails logged = createUserDetails();
        Visibility visibility = createVisibilityPublic();
        Mockito.when(mockVisibilitiesRepository.findByType(anyString()))
                .thenReturn(visibility);

        Page<Post> posts = new PageImpl(Arrays.asList(createPostConnected()));
        Mockito.when(mockRepository.findAllByCreatorAndVisibilityOrderByTimestampDesc(logged, visibility, pageable))
                .thenReturn(posts);

        // Act
        Collection<Post> list = mockService.getAllPublicUserPosts(logged, pageable);

        // Assert
        Assert.assertEquals(1, list.size());
    }

    @Test
    public void getAllPublicUserPostsShould_ReturnEmptyListOfPostsByTitle() {
        // Arrange
        UserDetails logged = createUserDetails();
        Visibility visibility = createVisibilityPublic();
        Mockito.when(mockVisibilitiesRepository.findByType(anyString()))
                .thenReturn(visibility);

        Page<Post> posts = Mockito.mock(Page.class);
        Mockito.when(mockRepository.findAllByCreatorAndVisibilityOrderByTimestampDesc(logged, visibility, pageable))
                .thenReturn(posts);

        // Act
        Collection<Post> list = mockService.getAllPublicUserPosts(logged, pageable);

        // Assert
        Assert.assertEquals(0, list.size());
    }

    @Test
    public void getTop3PostsShould_ReturnListOfPosts() {
        // Arrange
        Post posts = createPostConnected();
        Mockito.when(mockRepository.findTop3Post())
                .thenReturn(Arrays.asList(posts));

        // Act
        Collection<Post> list = mockService.getTop3Posts();

        // Assert
        Assert.assertEquals(1, list.size());
    }

    @Test
    public void getTop3PostsShould_ReturnEmptyListOfPostsByTitle() {
        // Arrange
        Mockito.when(mockRepository.findTop3Post())
                .thenReturn(Collections.emptyList());

        // Act
        Collection<Post> list = mockService.getTop3Posts();

        // Assert
        Assert.assertEquals(0, list.size());
    }

    @Test
    public void getPostByIdShould_ReturnPost_WhenPostExists() {
        // Arrange
        Post expected = createPostConnected();

        Mockito.when(mockRepository.findById(anyLong()))
                .thenReturn(Optional.of(expected));

        // Act
        Post actual = mockService.getPostById(anyLong());

        // Assert
        Assert.assertSame(expected, actual);
    }

    @Test
    public void getPostByIdShould_Throw_WhenPostNotExists() {
        // Arrange, Act, Assert
        Assert.assertThrows(EntityNotFoundException.class,
                () -> mockService.getPostById(anyLong()));
    }

    @Test
    public void getPostByIdShould_CallRepository() {
        // Arrange
        Post expected = createPostConnected();

        Mockito.when(mockRepository.findById(anyLong()))
                .thenReturn(Optional.of(expected));

        // Act
        mockService.getPostById(anyLong());

        // Assert
        Mockito.verify(mockRepository,
                times(1)).findById(anyLong());

    }

    @Test
    public void savePostShould_ReturnPost() {
        // Arrange
        Post expected = createPostConnected();

        Mockito.when(mockRepository.save(any(Post.class)))
                .thenReturn(expected);

        // Act
        Post actual = mockService.savePost(expected);

        // Assert
        Assert.assertSame(expected, actual);
    }

    @Test
    public void deletePostShould_CallRepository() {
        // Arrange
        Post post = createPostConnected();

        // Act
        mockService.deletePost(post);

        // Assert
        Mockito.verify(mockRepository, times(1)).delete(post);
    }

}
