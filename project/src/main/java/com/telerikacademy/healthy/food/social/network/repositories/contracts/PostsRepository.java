package com.telerikacademy.healthy.food.social.network.repositories.contracts;

import com.telerikacademy.healthy.food.social.network.models.Category;
import com.telerikacademy.healthy.food.social.network.models.Post;
import com.telerikacademy.healthy.food.social.network.models.UserDetails;
import com.telerikacademy.healthy.food.social.network.models.Visibility;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface PostsRepository extends JpaRepository<Post, Long>, JpaSpecificationExecutor<Post> {
    String WHERE_CLAUSE_ENABLED_TRUE = "p.enabled = true";
    String WHERE_CLAUSE_VISIBILITY_PUBLIC = "p.visibility_id = 1";
    String WHERE_CLAUSE_CONNECTED_USERS = WHERE_CLAUSE_ENABLED_TRUE + " && ( p.creator_id in (select sender_id from connections c where c.status_id = 1 && c.receiver_id = :id )\n" +
            "  || p.creator_id in (select receiver_id from connections c where c.status_id = 1 && c.sender_id = :id ))\n";

    String FROM_POSTS_LEFT_JOIN_LIKES = "from posts p left join posts_likes pl on p.post_id = pl.post_id\n";
    String FROM_POSTS_LEFT_JOIN_COMMENTS = "from posts p left join comments c on p.post_id = c.post_id\n";
    String FROM_POSTS_LEFT_JOIN_CATEGORIES = "from posts p left join posts_categories pc on p.post_id = pc.post_id\n";

    Page<Post> findAllByTitleLikeOrderByTimestampDesc(String title, Pageable pageable);

    Page<Post> findAllByCategoriesContainsOrderByTimestampDesc(Category category, Pageable pageable);

    @Query(nativeQuery = true, value = "select p.*\n" +
            FROM_POSTS_LEFT_JOIN_LIKES +
            "where " + WHERE_CLAUSE_ENABLED_TRUE + " " +
            "group by p.post_id\n" +
            "order by  count(pl.id) desc",
            countQuery = "select count(*)\n" +
                    FROM_POSTS_LEFT_JOIN_LIKES +
                    "where " + WHERE_CLAUSE_ENABLED_TRUE + " " +
                    "group by p.post_id\n")
    Page<Post> findAllOrderByLikes(Pageable pageable);

    @Query(nativeQuery = true, value = "select p.*\n" +
            FROM_POSTS_LEFT_JOIN_COMMENTS +
            "where " + WHERE_CLAUSE_ENABLED_TRUE + " " +
            "group by p.post_id\n" +
            "order by count(c.comment_id) desc",
            countQuery = "select count(*)\n" +
                    FROM_POSTS_LEFT_JOIN_COMMENTS +
                    "where " + WHERE_CLAUSE_ENABLED_TRUE + " " +
                    "group by p.post_id\n")
    Page<Post> findAllOrderByCommentsDesc(Pageable pageable);

    Page<Post> findAllByOrderByTimestampDesc(Pageable pageable);

    Page<Post> findAllByCreatorOrderByTimestampDesc(UserDetails user, Pageable pageable);

    Page<Post> findAllByCreatorAndVisibilityOrderByTimestampDesc(UserDetails user, Visibility visibility, Pageable pageable);

    @Query(nativeQuery = true, value = "select *\n" +
            "from posts p\n" +
            "where " + WHERE_CLAUSE_CONNECTED_USERS +
            "and p.title like CONCAT('%', :title, '%')\n" +
            "order by timestamp desc",
            countQuery = "select count(*)\n" +
                    "from posts p\n" +
                    "where " + WHERE_CLAUSE_CONNECTED_USERS +
                    "and p.title like CONCAT('%', :title, '%')\n")
    Page<Post> findAllConnectedUsersPostsByTitle(String title, long id, Pageable pageable);

    @Query(nativeQuery = true, value = "select p.*\n" +
            FROM_POSTS_LEFT_JOIN_CATEGORIES +
            "where pc.category_id = :categoryId && " + WHERE_CLAUSE_CONNECTED_USERS +
            "order by timestamp desc",
            countQuery = "select count(*)\n" +
                    FROM_POSTS_LEFT_JOIN_CATEGORIES + "\n" +
                    "where pc.category_id = :categoryId &&" + WHERE_CLAUSE_CONNECTED_USERS)
    Page<Post> findAllConnectedUsersPostsByCategory(int categoryId, long id, Pageable pageable);

    @Query(nativeQuery = true, value = "select p.*\n" +
            FROM_POSTS_LEFT_JOIN_LIKES +
            "where " + WHERE_CLAUSE_CONNECTED_USERS +
            "group by p.post_id\n" +
            "order by count(pl.id) desc",
            countQuery = "select count(*)\n" +
                    FROM_POSTS_LEFT_JOIN_LIKES +
                    "where " + WHERE_CLAUSE_CONNECTED_USERS +
                    "group by p.post_id\n")
    Page<Post> findAllConnectedUsersPostsOrderByLikes(long id, Pageable pageable);

    @Query(nativeQuery = true, value = "select p.*\n" +
            FROM_POSTS_LEFT_JOIN_COMMENTS +
            "where " + WHERE_CLAUSE_CONNECTED_USERS +
            "group by p.post_id\n" +
            "order by count(c.comment_id) desc",
            countQuery = "select count(*)\n" +
                    FROM_POSTS_LEFT_JOIN_COMMENTS +
                    "where " + WHERE_CLAUSE_CONNECTED_USERS +
                    "group by p.post_id\n")
    Page<Post> findAllConnectedUsersPostsOrderByComments(long id, Pageable pageable);

    @Query(nativeQuery = true, value = "select p.*\n" +
            "from posts p\n" +
            "where " + WHERE_CLAUSE_CONNECTED_USERS +
            "order by p.timestamp desc\n",
            countQuery = "select count(*)\n" +
                    "from posts p\n" +
                    "where " + WHERE_CLAUSE_CONNECTED_USERS)
    Page<Post> findAllConnectedUsersPostsOrderByDates(long id, Pageable pageable);

    Page<Post> findAllByVisibilityAndTitleLikeOrderByTimestampDesc(Visibility visibility, String title, Pageable pageable);

    Page<Post> findAllByVisibilityAndCategoriesContainsOrderByTimestampDesc(Visibility visibility, Category category, Pageable pageable);

    @Query(nativeQuery = true, value = "select p.*\n" +
            FROM_POSTS_LEFT_JOIN_LIKES +
            "where " + WHERE_CLAUSE_ENABLED_TRUE + " && " + WHERE_CLAUSE_VISIBILITY_PUBLIC + " \n" +
            "group by p.post_id\n" +
            "order by count(pl.id) desc",
            countQuery = "select count(p.post_id)\n" +
                    FROM_POSTS_LEFT_JOIN_LIKES +
                    "where " + WHERE_CLAUSE_ENABLED_TRUE + " && " + WHERE_CLAUSE_VISIBILITY_PUBLIC + "\n" +
                    "group by p.post_id\n")
    Page<Post> findAllPublicOrderByLikes(Pageable pageable);

    @Query(nativeQuery = true, value = "select p.*\n" +
            FROM_POSTS_LEFT_JOIN_COMMENTS +
            "where " + WHERE_CLAUSE_ENABLED_TRUE + " && " + WHERE_CLAUSE_VISIBILITY_PUBLIC + "\n" +
            "group by p.post_id\n" +
            "order by  count(c.comment_id) desc",
            countQuery = "select count(*)\n" +
                    FROM_POSTS_LEFT_JOIN_COMMENTS +
                    "where " + WHERE_CLAUSE_ENABLED_TRUE + " and " + WHERE_CLAUSE_VISIBILITY_PUBLIC + "\n")
    Page<Post> findAllPublicOrderByCommentsDesc(Pageable pageable);

    Page<Post> findAllByVisibilityOrderByTimestampDesc(Visibility visibility, Pageable pageable);

    @Query(nativeQuery = true, value = "select p.*\n" +
            FROM_POSTS_LEFT_JOIN_LIKES +
            "where " + WHERE_CLAUSE_ENABLED_TRUE + "&& " + WHERE_CLAUSE_VISIBILITY_PUBLIC + "\n" +
            "group by p.post_id\n" +
            "order by count(pl.post_id) desc\n" +
            "limit 3")
    Collection<Post> findTop3Post();
}
