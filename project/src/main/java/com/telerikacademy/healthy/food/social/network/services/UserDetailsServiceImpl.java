package com.telerikacademy.healthy.food.social.network.services;

import com.telerikacademy.healthy.food.social.network.exceptions.EntityNotFoundException;
import com.telerikacademy.healthy.food.social.network.models.UserDetails;
import com.telerikacademy.healthy.food.social.network.repositories.contracts.UserDetailsRepository;
import com.telerikacademy.healthy.food.social.network.services.contracts.UserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collection;

import static com.telerikacademy.healthy.food.social.network.utils.GlobalConstants.EMAIL;
import static com.telerikacademy.healthy.food.social.network.utils.GlobalConstants.USER;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserDetailsRepository userDetailsRepository;


    @Autowired
    public UserDetailsServiceImpl(UserDetailsRepository userDetailsRepository) {
        this.userDetailsRepository = userDetailsRepository;
    }

    @Override
    public Collection<UserDetails> getAll(String filter, Pageable pageable) {
        if (!filter.isEmpty()) {
            filter = "%" + filter + "%";
            return userDetailsRepository.findAllByFirstNameLikeAndEnabledTrueOrLastNameLikeAndEnabledTrueOrEmailLikeAndEnabledTrue(filter, filter, filter, pageable).getContent();
        }

        return userDetailsRepository.findAllByEnabledTrueOrderByFirstNameAsc(pageable).getContent();
    }

    @Override
    public UserDetails getUserDetailsById(long id) {
        return userDetailsRepository.findByIdAndEnabledTrue(id).orElseThrow(
                () -> new EntityNotFoundException(USER, id));
    }


    @Override
    public UserDetails getUserDetailsByEmail(String email) {
        return userDetailsRepository.findByEmailAndEnabledTrue(email).orElseThrow(
                () -> new EntityNotFoundException(USER, EMAIL, email));
    }

    @Override
    public UserDetails createUserDetails(UserDetails userDetails) {
        return userDetailsRepository.save(userDetails);
    }

    @Override
    public UserDetails updateUserDetails(UserDetails userDetails) {
        return userDetailsRepository.save(userDetails);
    }

    @Override
    public boolean adminPermission(long id) {
        return userDetailsRepository.adminPermission(id) > 0;
    }
}
