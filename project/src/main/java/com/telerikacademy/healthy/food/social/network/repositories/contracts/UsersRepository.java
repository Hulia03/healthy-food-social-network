package com.telerikacademy.healthy.food.social.network.repositories.contracts;

import com.telerikacademy.healthy.food.social.network.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersRepository extends JpaRepository<User, String> {
}
