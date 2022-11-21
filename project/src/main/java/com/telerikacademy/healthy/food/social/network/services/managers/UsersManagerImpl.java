package com.telerikacademy.healthy.food.social.network.services.managers;

import com.telerikacademy.healthy.food.social.network.models.UserDetails;
import com.telerikacademy.healthy.food.social.network.models.dtos.mappers.UserDetailsMapper;
import com.telerikacademy.healthy.food.social.network.models.dtos.users.UserDetailsDto;
import com.telerikacademy.healthy.food.social.network.services.ConnectionsServiceImpl;
import com.telerikacademy.healthy.food.social.network.services.contracts.UserDetailsService;
import com.telerikacademy.healthy.food.social.network.services.contracts.UsersService;
import com.telerikacademy.healthy.food.social.network.services.contracts.VisibilitiesService;
import com.telerikacademy.healthy.food.social.network.services.managers.contracts.UsersManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.telerikacademy.healthy.food.social.network.utils.GlobalConstants.CANNOT_MODIFY_USER_MESSAGE_FORMAT;


@Component
public class UsersManagerImpl implements UsersManager {
    private final UserDetailsMapper userDetailsMapper;
    private final UsersService usersService;
    private final UserDetailsService userDetailsService;
    private final ConnectionsServiceImpl connectionsService;
    private final VisibilitiesService visibilitiesService;

    @Autowired
    public UsersManagerImpl(UserDetailsMapper userDetailsMapper, UsersService usersService, UserDetailsService userDetailsService, ConnectionsServiceImpl connectionsService, VisibilitiesService visibilitiesService) {
        this.userDetailsMapper = userDetailsMapper;
        this.usersService = usersService;
        this.userDetailsService = userDetailsService;
        this.connectionsService = connectionsService;
        this.visibilitiesService = visibilitiesService;
    }

    @Override
    public Collection<UserDetailsDto> getAll(Collection<UserDetails> users) {
        List<UserDetailsDto> usersDetails = new ArrayList<>();
        for (UserDetails userDetails : users) {
            boolean isPublic = visibilitiesService.checkMediaPublic(userDetails.getPicture());
            usersDetails.add(userDetailsMapper.mergeUser(userDetails, isPublic));
        }

        return usersDetails;
    }

    @Override
    public Collection<UserDetailsDto> getAll(Collection<UserDetails> users, UserDetails logged) {
        List<UserDetailsDto> usersDetails = new ArrayList<>();
        for (UserDetails userDetails : users) {
            boolean isPublic = checkPublicPhoto(userDetails, logged);
            usersDetails.add(userDetailsMapper.mergeUser(userDetails, isPublic));
        }

        return usersDetails;
    }

    @Override
    public UserDetailsDto getUserDetailsByIdPublic(long id) {
        UserDetails userDetails = userDetailsService.getUserDetailsById(id);
        boolean isPublic = visibilitiesService.checkMediaPublic(userDetails.getPicture());
        return userDetailsMapper.mergeUser(userDetails, isPublic);
    }

    @Override
    public UserDetailsDto getUserDetailsById(long id, UserDetails logged) {
        UserDetails userDetails = userDetailsService.getUserDetailsById(id);
        boolean isPublic = checkPublicPhoto(userDetails, logged);
        return userDetailsMapper.mergeUser(userDetails, isPublic);
    }

    @Override
    public UserDetails updateUserDetails(UserDetails userDetails, UserDetails logged) {
        if(!userDetailsService.adminPermission(logged.getId()) &&
                logged.getId() != userDetails.getId()){
            throw new AccessDeniedException(String.format(CANNOT_MODIFY_USER_MESSAGE_FORMAT,userDetails.getId()));
        }
        return userDetailsService.updateUserDetails(userDetails);
    }

    @Override
    @Transactional
    public Collection<UserDetails> deleteUser(long id) {
        String username = userDetailsService.getUserDetailsById(id).getEmail();
        usersService.deleteUser(username);
        return userDetailsService.getAll("", Pageable.unpaged());
    }

    @Override
    public boolean checkPublicPhoto(UserDetails userDetails, UserDetails logged) {
        return connectionsService.checkUsersConnected(userDetails, logged)
                || userDetails.getId() == logged.getId()
                || visibilitiesService.checkMediaPublic(userDetails.getPicture())
                || userDetailsService.adminPermission(logged.getId());
    }
}
