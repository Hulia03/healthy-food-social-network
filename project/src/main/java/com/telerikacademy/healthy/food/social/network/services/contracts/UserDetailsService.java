package com.telerikacademy.healthy.food.social.network.services.contracts;

import com.telerikacademy.healthy.food.social.network.models.UserDetails;
import org.springframework.data.domain.Pageable;

import java.util.Collection;

public interface UserDetailsService {
    Collection<UserDetails> getAll(String filter, Pageable pageable);

    UserDetails getUserDetailsById(long id);

    UserDetails getUserDetailsByEmail(String email);

    UserDetails createUserDetails(UserDetails userDetails);

    UserDetails updateUserDetails(UserDetails userDetails);

    boolean adminPermission(long id);

}
