package com.telerikacademy.healthy.food.social.network.services;

import com.telerikacademy.healthy.food.social.network.exceptions.EntityNotFoundException;
import com.telerikacademy.healthy.food.social.network.models.ConfirmationToken;
import com.telerikacademy.healthy.food.social.network.models.User;
import com.telerikacademy.healthy.food.social.network.repositories.contracts.ConfirmationTokensRepository;
import com.telerikacademy.healthy.food.social.network.repositories.contracts.UsersRepository;
import com.telerikacademy.healthy.food.social.network.services.contracts.ConfirmationTokensService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.UUID;

import static com.telerikacademy.healthy.food.social.network.utils.GlobalConstants.*;

@Service
public class ConfirmationTokensServiceImpl implements ConfirmationTokensService {
    private final UsersRepository usersRepository;
    private final ConfirmationTokensRepository confirmationTokensRepository;

    @Autowired
    public ConfirmationTokensServiceImpl(UsersRepository usersRepository, ConfirmationTokensRepository confirmationTokensRepository) {
        this.usersRepository = usersRepository;
        this.confirmationTokensRepository = confirmationTokensRepository;
    }

    @Override
    public Collection<ConfirmationToken> getAll() {
        return confirmationTokensRepository.findAll();
    }

    @Override
    public ConfirmationToken getConfirmationTokenByName(String tokenName) {
        return confirmationTokensRepository.findByConfirmationToken(tokenName)
                .orElseThrow(
                        () -> new EntityNotFoundException(CONFIRMATION_TOKEN, NAME, tokenName));
    }

    @Override
    @Transactional
    public ConfirmationToken createConfirmationToken(String username) {
        User user = usersRepository.findById(username).orElseThrow(
                () -> new EntityNotFoundException(USER, EMAIL, username));

        ConfirmationToken confirmationToken = new ConfirmationToken();
        confirmationToken.setUser(user);
        confirmationToken.setCreatedDate(new Timestamp(System.currentTimeMillis()));
        confirmationToken.setConfirmationToken(UUID.randomUUID().toString());
        return confirmationTokensRepository.save(confirmationToken);
    }
}
