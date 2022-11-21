package com.telerikacademy.healthy.food.social.network.services.contracts;

import com.telerikacademy.healthy.food.social.network.models.ConfirmationToken;

import java.util.Collection;

public interface ConfirmationTokensService {
    Collection<ConfirmationToken> getAll();

    ConfirmationToken getConfirmationTokenByName(String tokenName);

    ConfirmationToken createConfirmationToken(String username);
}
