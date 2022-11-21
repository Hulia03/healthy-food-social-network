package com.telerikacademy.healthy.food.social.network.services;

import com.telerikacademy.healthy.food.social.network.exceptions.EntityNotFoundException;
import com.telerikacademy.healthy.food.social.network.models.Gender;
import com.telerikacademy.healthy.food.social.network.repositories.contracts.GenderRepository;
import com.telerikacademy.healthy.food.social.network.services.contracts.GenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

import static com.telerikacademy.healthy.food.social.network.utils.GlobalConstants.GENDER;

@Service
public class GenderServiceImpl implements GenderService {
    private final GenderRepository genderRepository;

    @Autowired
    public GenderServiceImpl(GenderRepository genderRepository) {
        this.genderRepository = genderRepository;
    }

    @Override
    public Collection<Gender> getAll() {
        return genderRepository.findAll();
    }

    @Override
    public Gender getGenderById(int id) {
        return genderRepository.findById(id)
                .orElseThrow(
                        () -> new EntityNotFoundException(GENDER, id));
    }
}
