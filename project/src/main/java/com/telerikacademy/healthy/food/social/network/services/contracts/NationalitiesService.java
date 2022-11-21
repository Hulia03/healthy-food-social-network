package com.telerikacademy.healthy.food.social.network.services.contracts;

import com.telerikacademy.healthy.food.social.network.models.Nationality;

import java.util.Collection;

public interface NationalitiesService {
    Collection<Nationality> getAll();

    Nationality getNationalityById(int id);

    Nationality createNationality(Nationality nationality);

    Nationality updateNationality(Nationality nationality);

    void deleteNationality(int id);
}
