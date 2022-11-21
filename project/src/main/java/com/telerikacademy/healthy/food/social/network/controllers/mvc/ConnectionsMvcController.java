package com.telerikacademy.healthy.food.social.network.controllers.mvc;

import com.telerikacademy.healthy.food.social.network.models.Connection;
import com.telerikacademy.healthy.food.social.network.models.UserDetails;
import com.telerikacademy.healthy.food.social.network.services.contracts.ConnectionsService;
import com.telerikacademy.healthy.food.social.network.services.contracts.UserDetailsService;
import com.telerikacademy.healthy.food.social.network.services.managers.contracts.UsersManager;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

import static com.telerikacademy.healthy.food.social.network.utils.GlobalConstants.CONNECTIONS_URL;

@Controller
@RequestMapping(CONNECTIONS_URL)
public class ConnectionsMvcController {
    private final ConnectionsService connectionsService;
    private final UserDetailsService userDetailsService;
    private final UsersManager usersManager;

    @Autowired
    public ConnectionsMvcController(ConnectionsService connectionsService, UserDetailsService userDetailsService, UsersManager usersManager) {
        this.connectionsService = connectionsService;
        this.userDetailsService = userDetailsService;
        this.usersManager = usersManager;
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping
    public String showConnected() {
        return "users/friends";
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/paging")
    @ApiOperation(value = "List of all connected users", response = Collection.class)
    public String getAllMyConnectedUsers(@RequestParam(required = false, defaultValue = "0") int page,
                                         @RequestParam(required = false, defaultValue = "5") int size,
                                         final Model model,
                                         final Authentication auth) {
        Pageable pageable = PageRequest.of(page, size);
        UserDetails logged = userDetailsService.getUserDetailsByEmail(auth.getName());
        Collection<UserDetails> connections = connectionsService.getAllConnectedUsers(logged, pageable);
        model.addAttribute("users", usersManager.getAll(connections, logged));
        return "fragments/user :: users";
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/requests")
    public String showRequests() {
        return "users/requests";
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/requests/paging")
    @ApiOperation(value = "List of all user`s requests", response = Collection.class)
    public String getAllRequestedUsers(@RequestParam int page,
                                       @RequestParam int size,
                                       final Model model,
                                       final Authentication auth) {
        Pageable pageable = PageRequest.of(page, size);
        UserDetails logged = userDetailsService.getUserDetailsByEmail(auth.getName());
        Collection<UserDetails> requests = connectionsService.getAllRequestedUsers(logged, pageable);
        model.addAttribute("users", usersManager.getAll(requests, logged));
        return "fragments/user :: users";
    }

    @PostMapping("/users/{user_id}/send")
    public String sendRequest(@PathVariable(name = "user_id") long receiverId, final Authentication auth) {
        UserDetails logged = userDetailsService.getUserDetailsByEmail(auth.getName());
        connectionsService.sendRequest(receiverId, logged);
        return "redirect:/users/" + receiverId;
    }

    @PostMapping("/{id}/confirm")
    public String confirmRequest(@PathVariable long id, final Authentication auth) {
        UserDetails logged = userDetailsService.getUserDetailsByEmail(auth.getName());
        UserDetails sender = connectionsService.confirmRequest(id, logged);
        return "redirect:/users/" + sender.getId();
    }

    @PostMapping("/{id}/reject")
    public String rejectConnection(@PathVariable long id, final Authentication auth) {
        UserDetails logged = userDetailsService.getUserDetailsByEmail(auth.getName());
        Connection connection = connectionsService.getConnectionById(id);
        long rejectedUserId = connection.getSender().getId() == logged.getId() ? connection.getReceiver().getId() : connection.getSender().getId();
        connectionsService.rejectRequest(id, logged);
        return "redirect:/users/" + rejectedUserId;
    }
}
