package com.telerikacademy.healthy.food.social.network.services;

import com.telerikacademy.healthy.food.social.network.exceptions.EntityNotFoundException;
import com.telerikacademy.healthy.food.social.network.models.User;
import com.telerikacademy.healthy.food.social.network.models.UserDetails;
import com.telerikacademy.healthy.food.social.network.repositories.contracts.UserDetailsRepository;
import com.telerikacademy.healthy.food.social.network.repositories.contracts.UsersRepository;
import com.telerikacademy.healthy.food.social.network.services.contracts.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

import static com.telerikacademy.healthy.food.social.network.utils.GlobalConstants.EMAIL;
import static com.telerikacademy.healthy.food.social.network.utils.GlobalConstants.USER;

@Service
public class UsersServiceImpl implements UsersService {
    private final UsersRepository usersRepository;
    private final UserDetailsRepository userDetailsRepository;

    @Autowired
    public UsersServiceImpl(UsersRepository usersRepository, UserDetailsRepository userDetailsRepository) {
        this.usersRepository = usersRepository;
        this.userDetailsRepository = userDetailsRepository;
    }

    @Override
    public Collection<User> getAll() {
        return usersRepository.findAll();
    }

    @Override
    public User getUserByName(String username) {
        return usersRepository.findById(username)
                .orElseThrow(
                        () -> new EntityNotFoundException(USER, EMAIL, username)
                );
    }

    @Override
    public User updateUser(User user) {
        if (!usersRepository.existsById(user.getUsername())) {
            throw new EntityNotFoundException(USER, EMAIL, user.getUsername());
        }

        return usersRepository.save(user);
    }

    @Override
    @Transactional
    public void deleteUser(String username) {
        User user = usersRepository.findById(username).orElseThrow(
                () -> new EntityNotFoundException(USER, EMAIL, username)
        );

        user.setEnabled(false);
        usersRepository.save(user);

        UserDetails userDetails = userDetailsRepository.findByEmailAndEnabledTrue(username).orElseThrow(
                () -> new EntityNotFoundException(USER, EMAIL, username)
        );

        userDetails.setEnabled(false);
        userDetailsRepository.save(userDetails);
    }

    @Override
    @Transactional
    public void confirmUser(String username) {
        User user = getUserByName(username);
        user.setEnabled(true);
        usersRepository.save(user);

        UserDetails userDetails = userDetailsRepository.findByEmail(username).orElseThrow(
                () -> new EntityNotFoundException(USER, EMAIL, username)
        );

        userDetails.setEnabled(true);
        userDetailsRepository.save(userDetails);
    }
}
