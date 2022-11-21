package com.telerikacademy.healthy.food.social.network.repositories.contracts;

import com.telerikacademy.healthy.food.social.network.models.UserDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserDetailsRepository extends JpaRepository<UserDetails, Long> {
    Page<UserDetails> findAllByEnabledTrueOrderByFirstNameAsc(Pageable pageable);

    Page<UserDetails> findAllByFirstNameLikeAndEnabledTrueOrLastNameLikeAndEnabledTrueOrEmailLikeAndEnabledTrue(String firstName, String lastName, String email, Pageable pageable);

    Optional<UserDetails> findByIdAndEnabledTrue(long id);

    Optional<UserDetails> findByEmailAndEnabledTrue(String email);

    Optional<UserDetails> findByEmail(String email);

    @Query(nativeQuery = true, value = "select COUNT(u.user_details_id) \n" +
            "from users_details u\n" +
            "join users u2 on u.email = u2.username\n" +
            "join authorities a on u2.username = a.username\n" +
            "where authority = 'ROLE_ADMIN' and u.user_details_id = :id")
    int adminPermission(long id);
}
