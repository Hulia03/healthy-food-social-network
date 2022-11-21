package com.telerikacademy.healthy.food.social.network.repositories.contracts;

import com.telerikacademy.healthy.food.social.network.models.Comment;
import com.telerikacademy.healthy.food.social.network.models.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface CommentsRepository extends JpaRepository<Comment, Long> {
    Collection<Comment> findAllByPostOrderByTimestampDesc(Post post);

    @Query(nativeQuery = true, value = "select *\n" +
            "from comments\n" +
            "where post_id = :postId and enabled = true\n" +
            "order by timestamp desc\n" +
            "limit 5")
    Collection<Comment> findLatest5Comments(Long postId);
}
