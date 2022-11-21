package com.telerikacademy.healthy.food.social.network.repositories.contracts;

import com.telerikacademy.healthy.food.social.network.models.MediaType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MediaTypesRepository extends JpaRepository<MediaType, Integer> {
    Optional<MediaType> findByType(String type);
}
