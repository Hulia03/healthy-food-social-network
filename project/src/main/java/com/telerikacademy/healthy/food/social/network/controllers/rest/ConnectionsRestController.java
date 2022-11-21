package com.telerikacademy.healthy.food.social.network.controllers.rest;

import com.telerikacademy.healthy.food.social.network.models.Connection;
import com.telerikacademy.healthy.food.social.network.models.UserDetails;
import com.telerikacademy.healthy.food.social.network.models.dtos.users.UserDetailsDto;
import com.telerikacademy.healthy.food.social.network.services.contracts.ConnectionsService;
import com.telerikacademy.healthy.food.social.network.services.contracts.UserDetailsService;
import com.telerikacademy.healthy.food.social.network.services.managers.contracts.UsersManager;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

import static com.telerikacademy.healthy.food.social.network.utils.GlobalConstants.API_URL;
import static com.telerikacademy.healthy.food.social.network.utils.GlobalConstants.CONNECTIONS_URL;

@RestController
@RequestMapping(API_URL + CONNECTIONS_URL)
public class ConnectionsRestController {
    private final ConnectionsService connectionsService;
    private final UserDetailsService userDetailsService;
    private final UsersManager usersManager;

    @Autowired
    public ConnectionsRestController(ConnectionsService connectionsService, UserDetailsService userDetailsService, UsersManager usersManager) {
        this.connectionsService = connectionsService;
        this.userDetailsService = userDetailsService;
        this.usersManager = usersManager;
    }

    @GetMapping("/my")
    @ApiOperation(value = "List of all connected users", response = Collection.class)
    public Collection<UserDetailsDto> getAllMyConnectedUsers(@RequestParam(required = false, defaultValue = "0") int page,
                                                             @RequestParam(required = false, defaultValue = "5") int size,
                                                             final Authentication auth) {
        Pageable pageable = PageRequest.of(page, size);
        UserDetails logged = userDetailsService.getUserDetailsByEmail(auth.getName());
        Collection<UserDetails> connections = connectionsService.getAllConnectedUsers(logged, pageable);
        return usersManager.getAll(connections);
    }

    @GetMapping("/requests")
    @ApiOperation(value = "List of all user`s requests", response = Collection.class)
    public Collection<UserDetailsDto> getAllRequestedUsers(@RequestParam(required = false, defaultValue = "0") int page,
                                                           @RequestParam(required = false, defaultValue = "5") int size,
                                                           final Authentication auth) {
        Pageable pageable = PageRequest.of(page, size);
        UserDetails logged = userDetailsService.getUserDetailsByEmail(auth.getName());
        Collection<UserDetails> requests = connectionsService.getAllRequestedUsers(logged, pageable);
        return usersManager.getAll(requests);
    }

    @PostMapping("/users/{user_id}")
    @ApiOperation(value = "Send connection request", notes = "Return list of all user`s requests", response = Collection.class)
    public Collection<UserDetailsDto> sendRequest(@PathVariable(name = "user_id") long receiverId, final Authentication auth) {
        UserDetails logged = userDetailsService.getUserDetailsByEmail(auth.getName());
        connectionsService.sendRequest(receiverId, logged);
        Collection<UserDetails> requests = connectionsService.getAllRequestedUsers(logged, Pageable.unpaged());
        return usersManager.getAll(requests);
    }

    @PutMapping("/{id}")
    @ApiOperation(value = "Confirm connection", response = Connection.class)
    public UserDetails confirmRequest(@PathVariable long id, final Authentication auth) {
        UserDetails logged = userDetailsService.getUserDetailsByEmail(auth.getName());
        return connectionsService.confirmRequest(id, logged);
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Reject connection", notes = "Return list of all connected users", response = Collection.class)
    public Collection<UserDetails> rejectConnection(@PathVariable long id, final Authentication auth) {
        UserDetails logged = userDetailsService.getUserDetailsByEmail(auth.getName());
        connectionsService.rejectRequest(id, logged);
        return connectionsService.getAllConnectedUsers(logged, Pageable.unpaged());
    }
}
