package com.telerikacademy.healthy.food.social.network.repositories.contracts;

import com.telerikacademy.healthy.food.social.network.models.Nationality;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NationalitiesRepository extends JpaRepository<Nationality, Integer> {
    Boolean existsNationalitiesByIdAndNationality(int id, String name);

    Optional<Nationality> findByNationality(String nationality);
}
