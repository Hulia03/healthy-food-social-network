package com.telerikacademy.healthy.food.social.network.services;

import com.telerikacademy.healthy.food.social.network.exceptions.EntityNotFoundException;
import com.telerikacademy.healthy.food.social.network.models.Category;
import com.telerikacademy.healthy.food.social.network.models.Post;
import com.telerikacademy.healthy.food.social.network.models.UserDetails;
import com.telerikacademy.healthy.food.social.network.models.Visibility;
import com.telerikacademy.healthy.food.social.network.repositories.contracts.CategoriesRepository;
import com.telerikacademy.healthy.food.social.network.repositories.contracts.PostsRepository;
import com.telerikacademy.healthy.food.social.network.repositories.contracts.VisibilitiesRepository;
import com.telerikacademy.healthy.food.social.network.services.contracts.PostsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collection;

import static com.telerikacademy.healthy.food.social.network.utils.GlobalConstants.*;

@Service
public class PostsServiceImpl implements PostsService {
    private final PostsRepository postsRepository;
    private final VisibilitiesRepository visibilityRepository;
    private final CategoriesRepository categoriesRepository;

    @Autowired
    public PostsServiceImpl(PostsRepository postsRepository, VisibilitiesRepository visibilityRepository, CategoriesRepository categoriesRepository) {
        this.postsRepository = postsRepository;
        this.visibilityRepository = visibilityRepository;
        this.categoriesRepository = categoriesRepository;
    }

    @Override
    public Collection<Post> getAll(String title, int categoryId, String sort, Pageable pageable) {
        if (!title.isEmpty()) {
            title = "%" + title + "%";
            return postsRepository.findAllByTitleLikeOrderByTimestampDesc(title, pageable).getContent();
        }

        if (categoryId > 0) {
            Category category = categoriesRepository.findById(categoryId).orElseThrow(
                    () -> new EntityNotFoundException(CATEGORY, categoryId)
            );

            return postsRepository.findAllByCategoriesContainsOrderByTimestampDesc(category, pageable).getContent();
        }

        if (sort.equals(LIKES)) {
            return postsRepository.findAllOrderByLikes(pageable).getContent();
        }

        if (sort.equals(COMMENTS)) {
            return postsRepository.findAllOrderByCommentsDesc(pageable).getContent();
        }

        return postsRepository.findAllByOrderByTimestampDesc(pageable).getContent();
    }

    @Override
    public Collection<Post> getAllPublic(String title, int categoryId, String sort, Pageable pageable) {
        Visibility visibility = visibilityRepository.findByType(PUBLIC);

        if (!title.isEmpty()) {
            title = "%" + title + "%";
            return postsRepository.findAllByVisibilityAndTitleLikeOrderByTimestampDesc(visibility, title, pageable).getContent();
        }

        if (categoryId > 0) {
            Category category = categoriesRepository.findById(categoryId).orElseThrow(
                    () -> new EntityNotFoundException(CATEGORY, categoryId)
            );

            return postsRepository.findAllByVisibilityAndCategoriesContainsOrderByTimestampDesc(visibility, category, pageable).getContent();
        }

        if (sort.equals(LIKES)) {
            return postsRepository.findAllPublicOrderByLikes(pageable).getContent();
        }

        if (sort.equals(COMMENTS)) {
            return postsRepository.findAllPublicOrderByCommentsDesc(pageable).getContent();
        }

        return postsRepository.findAllByVisibilityOrderByTimestampDesc(visibility, pageable).getContent();
    }

    @Override
    public Collection<Post> getAllConnectedUsersPost(String title, int categoryId, String sort, UserDetails logged, Pageable pageable) {
        if (!title.isEmpty()) {
            return postsRepository.findAllConnectedUsersPostsByTitle(title, logged.getId(), pageable).getContent();
        }

        if (categoryId > 0) {
            return postsRepository.findAllConnectedUsersPostsByCategory(categoryId, logged.getId(), pageable).getContent();
        }

        if (sort.equals(LIKES)) {
            return postsRepository.findAllConnectedUsersPostsOrderByLikes(logged.getId(), pageable).getContent();
        }

        if (sort.equals(COMMENTS)) {
            return postsRepository.findAllConnectedUsersPostsOrderByComments(logged.getId(), pageable).getContent();
        }

        return postsRepository.findAllConnectedUsersPostsOrderByDates(logged.getId(), pageable).getContent();
    }

    @Override
    public Collection<Post> getAllUserPosts(UserDetails user, Pageable pageable) {
        return postsRepository.findAllByCreatorOrderByTimestampDesc(user, pageable).getContent();
    }

    @Override
    public Collection<Post> getAllPublicUserPosts(UserDetails user, Pageable pageable) {
        Visibility publicPosts = visibilityRepository.findByType(PUBLIC);
        return postsRepository.findAllByCreatorAndVisibilityOrderByTimestampDesc(user, publicPosts, pageable).getContent();
    }

    @Override
    public Collection<Post> getTop3Posts() {
        return postsRepository.findTop3Post();
    }

    @Override
    public Post getPostById(long id) {
        return postsRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(POST, id)
        );
    }

    @Override
    public Post savePost(Post post) {
        return postsRepository.save(post);
    }

    @Override
    public void deletePost(Post post) {
        postsRepository.delete(post);
    }
}
