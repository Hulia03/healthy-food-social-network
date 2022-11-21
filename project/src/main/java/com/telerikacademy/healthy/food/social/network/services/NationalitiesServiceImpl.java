package com.telerikacademy.healthy.food.social.network.services;

import com.telerikacademy.healthy.food.social.network.exceptions.DuplicateEntityException;
import com.telerikacademy.healthy.food.social.network.exceptions.EntityNotFoundException;
import com.telerikacademy.healthy.food.social.network.models.Nationality;
import com.telerikacademy.healthy.food.social.network.repositories.contracts.NationalitiesRepository;
import com.telerikacademy.healthy.food.social.network.services.contracts.NationalitiesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

import static com.telerikacademy.healthy.food.social.network.utils.GlobalConstants.NAME;
import static com.telerikacademy.healthy.food.social.network.utils.GlobalConstants.NATIONALITY;

@Service
public class NationalitiesServiceImpl implements NationalitiesService {
    private final NationalitiesRepository nationalitiesRepository;

    @Autowired
    public NationalitiesServiceImpl(NationalitiesRepository nationalitiesRepository) {
        this.nationalitiesRepository = nationalitiesRepository;
    }

    @Override
    public Collection<Nationality> getAll() {
        return nationalitiesRepository.findAll();
    }

    @Override
    public Nationality getNationalityById(int id) {
        return nationalitiesRepository.findById(id)
                .orElseThrow(
                        () -> new EntityNotFoundException(NATIONALITY, id)
                );
    }

    @Override
    @Transactional
    public Nationality createNationality(Nationality nationality) {
        throwExceptionWhenNationalityNameExist(nationality.getNationality());
        return nationalitiesRepository.save(nationality);
    }

    @Override
    @Transactional
    public Nationality updateNationality(Nationality nationality) {
        throwExceptionWhenNationalityNotExist(nationality.getId());
        throwExceptionWhenNationalityNameWithNewNameExist(nationality.getId(), nationality.getNationality());
        return nationalitiesRepository.save(nationality);
    }

    @Override
    @Transactional
    public void deleteNationality(int id) {
        throwExceptionWhenNationalityNotExist(id);
        nationalitiesRepository.deleteById(id);
    }

    private void throwExceptionWhenNationalityNotExist(int id) {
        if (!nationalitiesRepository.existsById(id)) {
            throw new EntityNotFoundException(NATIONALITY, id);
        }
    }

    private void throwExceptionWhenNationalityNameExist(String nationality) {
        if (nationalitiesRepository.findByNationality(nationality).isPresent()) {
            throw new DuplicateEntityException(NATIONALITY, NAME, nationality);
        }
    }

    private void throwExceptionWhenNationalityNameWithNewNameExist(int id, String nationality) {
        boolean nationalityChanged = !nationalitiesRepository.existsNationalitiesByIdAndNationality(id, nationality);
        boolean nationalityExist = nationalitiesRepository.findByNationality(nationality).isPresent();
        if (nationalityChanged && nationalityExist) {
            throw new DuplicateEntityException(NATIONALITY, NAME, nationality);
        }
    }
}
