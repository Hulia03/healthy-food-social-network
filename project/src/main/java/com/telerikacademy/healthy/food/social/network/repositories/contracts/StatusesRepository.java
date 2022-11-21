package com.telerikacademy.healthy.food.social.network.repositories.contracts;

import com.telerikacademy.healthy.food.social.network.models.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StatusesRepository extends JpaRepository<Status, Integer> {
    Status findByType(String type);
}
