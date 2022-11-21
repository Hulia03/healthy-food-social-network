package com.telerikacademy.healthy.food.social.network.services.contracts;

import com.telerikacademy.healthy.food.social.network.models.Gender;

import java.util.Collection;

public interface GenderService {
    Collection<Gender> getAll();

    Gender getGenderById(int id);
}
