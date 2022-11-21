package com.telerikacademy.healthy.food.social.network.controllers.rest;

import com.telerikacademy.healthy.food.social.network.models.UserDetails;
import com.telerikacademy.healthy.food.social.network.models.dtos.mappers.UserDetailsMapper;
import com.telerikacademy.healthy.food.social.network.models.dtos.users.UserDetailsDto;
import com.telerikacademy.healthy.food.social.network.models.dtos.users.UserDetailsUpdateDto;
import com.telerikacademy.healthy.food.social.network.services.contracts.UserDetailsService;
import com.telerikacademy.healthy.food.social.network.services.managers.contracts.UsersManager;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.Collection;

import static com.telerikacademy.healthy.food.social.network.utils.GlobalConstants.API_URL;
import static com.telerikacademy.healthy.food.social.network.utils.GlobalConstants.USERS_URL;

@RestController
@RequestMapping(API_URL + USERS_URL)
public class UsersRestController {
    private final UsersManager usersManager;
    private final UserDetailsService userDetailsService;
    private final UserDetailsMapper userDetailsMapper;

    @Autowired
    public UsersRestController(UsersManager usersManager, UserDetailsService userDetailsService, UserDetailsMapper userDetailsMapper) {
        this.usersManager = usersManager;
        this.userDetailsService = userDetailsService;
        this.userDetailsMapper = userDetailsMapper;
    }

    @GetMapping
    @ApiOperation(value = "List of all user's details", response = Collection.class)
    public Collection<UserDetailsDto> getUserDetails(@RequestParam(required = false, defaultValue = "0") int page,
                                                     @RequestParam(required = false, defaultValue = "5") int size,
                                                     @RequestParam(required = false, defaultValue = "") String filter,
                                                     final Authentication auth) {
        Pageable pageable = PageRequest.of(page, size);
        Collection<UserDetails> users = userDetailsService.getAll(filter, pageable);
        if (auth == null) {
            return usersManager.getAll(users);
        } else {
            UserDetails logged = userDetailsService.getUserDetailsByEmail(auth.getName());
            return usersManager.getAll(users, logged);
        }
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Get user's details by id", response = UserDetailsDto.class)
    public UserDetailsDto getUserDetailsById(@PathVariable long id, final Authentication auth) {
        if (auth == null) {
            return usersManager.getUserDetailsByIdPublic(id);
        }

        UserDetails logged = userDetailsService.getUserDetailsByEmail(auth.getName());
        return usersManager.getUserDetailsById(id, logged);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PutMapping("/{id}")
    @ApiOperation(value = "Update user", response = UserDetails.class)
    public UserDetails updateUser(@PathVariable long id,
                                  @RequestPart(required = false) MultipartFile file,
                                  @RequestPart("user") @Valid UserDetailsUpdateDto dto,
                                  final Authentication auth) {
        dto.setPicture(file);
        UserDetails oldUser = userDetailsService.getUserDetailsById(id);
        UserDetails userDetails = userDetailsMapper.mergeUser(oldUser, dto);
        UserDetails logged = userDetailsService.getUserDetailsByEmail(auth.getName());
        return usersManager.updateUserDetails(userDetails, logged);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    @ApiOperation(value = "Delete user", notes = "Return list of all user's details", response = Collection.class)
    public Collection<UserDetails> deleteUser(@PathVariable int id) {
        return usersManager.deleteUser(id);
    }
}
