package com.telerikacademy.healthy.food.social.network.services.contracts;

import com.telerikacademy.healthy.food.social.network.models.User;

import java.util.Collection;

public interface UsersService {
    Collection<User> getAll();

    User getUserByName(String username);

    User updateUser(User user);

    void deleteUser(String username);

    void confirmUser(String username);
}
