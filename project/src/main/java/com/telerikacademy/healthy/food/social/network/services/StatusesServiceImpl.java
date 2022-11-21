package com.telerikacademy.healthy.food.social.network.services;

import com.telerikacademy.healthy.food.social.network.exceptions.EntityNotFoundException;
import com.telerikacademy.healthy.food.social.network.models.Status;
import com.telerikacademy.healthy.food.social.network.repositories.contracts.StatusesRepository;
import com.telerikacademy.healthy.food.social.network.services.contracts.StatusesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

import static com.telerikacademy.healthy.food.social.network.utils.GlobalConstants.*;

@Service
public class StatusesServiceImpl implements StatusesService {
    private final StatusesRepository statusesRepository;

    @Autowired
    public StatusesServiceImpl(StatusesRepository statusesRepository) {
        this.statusesRepository = statusesRepository;
    }

    @Override
    public Collection<Status> getAll() {
        return statusesRepository.findAll();
    }

    @Override
    public Status getStatusById(int id) {
        return statusesRepository.findById(id)
                .orElseThrow(
                        () -> new EntityNotFoundException(STATUS, id)
                );
    }

    @Override
    public Status getStatusByType(String type) {
        Status status = statusesRepository.findByType(type);
        if (status == null) {
            throw new EntityNotFoundException(STATUS, TYPE, type);
        }

        return status;
    }
}
