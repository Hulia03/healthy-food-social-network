package com.telerikacademy.healthy.food.social.network.services.managers.contracts;

import com.telerikacademy.healthy.food.social.network.models.UserDetails;
import com.telerikacademy.healthy.food.social.network.models.dtos.users.UserDetailsDto;

import java.util.Collection;

public interface UsersManager {
    Collection<UserDetailsDto> getAll(Collection<UserDetails> users);

    Collection<UserDetailsDto> getAll(Collection<UserDetails> users, UserDetails logged);

    UserDetailsDto getUserDetailsByIdPublic(long id);

    UserDetailsDto getUserDetailsById(long id, UserDetails logged);

    UserDetails updateUserDetails(UserDetails userDetails, UserDetails logged);

    Collection<UserDetails> deleteUser(long id);

    boolean checkPublicPhoto(UserDetails userDetails, UserDetails logged);
}
