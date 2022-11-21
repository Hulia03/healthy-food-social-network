package com.telerikacademy.healthy.food.social.network.services.contracts;

import com.telerikacademy.healthy.food.social.network.models.Status;

import java.util.Collection;

public interface StatusesService {
    Collection<Status> getAll();

    Status getStatusById(int id);

    Status getStatusByType(String type);
}
