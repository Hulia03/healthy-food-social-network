package com.telerikacademy.healthy.food.social.network.controllers.mvc;

import com.telerikacademy.healthy.food.social.network.models.Gender;
import com.telerikacademy.healthy.food.social.network.models.Nationality;
import com.telerikacademy.healthy.food.social.network.models.UserDetails;
import com.telerikacademy.healthy.food.social.network.models.Visibility;
import com.telerikacademy.healthy.food.social.network.models.dtos.mappers.UserDetailsMapper;
import com.telerikacademy.healthy.food.social.network.models.dtos.users.UserDetailsUpdateDto;
import com.telerikacademy.healthy.food.social.network.services.contracts.*;
import com.telerikacademy.healthy.food.social.network.services.managers.contracts.UsersManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collection;

import static com.telerikacademy.healthy.food.social.network.utils.GlobalConstants.CANNOT_MODIFY_USER_MESSAGE_FORMAT;
import static com.telerikacademy.healthy.food.social.network.utils.GlobalConstants.USERS_URL;

@Controller
@RequestMapping(USERS_URL)
public class UsersMvcController {
    private final UsersManager usersManager;
    private final UserDetailsMapper userDetailsMapper;
    private final UserDetailsService userDetailsService;
    private final NationalitiesService nationalitiesService;
    private final GenderService genderService;
    private final VisibilitiesService visibilityService;
    private final ConnectionsService connectionService;

    @Autowired
    public UsersMvcController(UsersManager usersManager, UserDetailsMapper userDetailsMapper, UserDetailsService userDetailsService, NationalitiesService nationalitiesService, GenderService genderService, VisibilitiesService visibilityService, ConnectionsService connectionService) {
        this.usersManager = usersManager;
        this.userDetailsMapper = userDetailsMapper;
        this.userDetailsService = userDetailsService;
        this.nationalitiesService = nationalitiesService;
        this.genderService = genderService;
        this.visibilityService = visibilityService;
        this.connectionService = connectionService;
    }

    @GetMapping
    public String showUsers() {
        return "users/users";
    }

    @GetMapping("/paging")
    public String showUsers(@RequestParam(required = false, defaultValue = "0") int page,
                            @RequestParam(required = false, defaultValue = "5") int size,
                            @RequestParam(required = false, defaultValue = "") String filter,
                            final Model model, final Authentication auth) {
        Pageable pageable = PageRequest.of(page, size);
        Collection<UserDetails> users = userDetailsService.getAll(filter, pageable);
        if (auth == null) {
            model.addAttribute("users", usersManager.getAll(users));
        } else {
            UserDetails logged = userDetailsService.getUserDetailsByEmail(auth.getName());
            model.addAttribute("users", usersManager.getAll(users, logged));
        }

        return "fragments/user :: users";
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/profile")
    public String showProfilePage(final Model model, final Authentication auth) {
        UserDetails user = userDetailsService.getUserDetailsByEmail(auth.getName());
        model.addAttribute("user", user);
        return "redirect:/users/" + user.getId();
    }

    @GetMapping("/{id}")
    public String showProfile(@PathVariable long id, final Model model, final Authentication auth) {
        if (auth == null) {
            model.addAttribute("user", usersManager.getUserDetailsByIdPublic(id));
        } else {
            UserDetails logged = userDetailsService.getUserDetailsByEmail(auth.getName());
            model.addAttribute("user", usersManager.getUserDetailsById(id, logged));
            model.addAttribute("connection", connectionService.getConnectionBySenderOrReceiver(logged, userDetailsService.getUserDetailsById(id)));
        }

        return "users/profile";
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/update")
    public String showUpdatePage(final Authentication auth) {
        UserDetails user = userDetailsService.getUserDetailsByEmail(auth.getName());
        return "redirect:/users/" + user.getId() + "/update";
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/{id}/update")
    public String showUpdateUserForm(@PathVariable long id, final Model model, final Authentication auth) {
        UserDetails logged = userDetailsService.getUserDetailsByEmail(auth.getName());
        boolean isAdmin = userDetailsService.adminPermission(logged.getId());
        if (logged.getId() != id && !isAdmin) {
            throw new AccessDeniedException(String.format(CANNOT_MODIFY_USER_MESSAGE_FORMAT, id));
        }

        UserDetails oldUser = userDetailsService.getUserDetailsById(id);
        UserDetailsUpdateDto user = userDetailsMapper.mergeUser(oldUser);
        model.addAttribute("user", user);
        return "users/update-user";
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/{id}/update")
    public String updateUser(@PathVariable long id,
                             @Valid @ModelAttribute("user") UserDetailsUpdateDto dto,
                             BindingResult errors,
                             final Authentication auth) {
        if (errors.hasErrors()) {
            return "users/update-user";
        }

        UserDetails oldUser = userDetailsService.getUserDetailsById(id);
        UserDetails userDetails = userDetailsMapper.mergeUser(oldUser, dto);
        UserDetails logged = userDetailsService.getUserDetailsByEmail(auth.getName());
        usersManager.updateUserDetails(userDetails, logged);
        return "redirect:/users/" + id;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/{id}/delete")
    public String showDeleteUserForm(@PathVariable long id, final Model model) {
        UserDetails userDetailsById = userDetailsService.getUserDetailsById(id);
        model.addAttribute("user", userDetailsById);
        return "users/delete-user";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/{id}/delete")
    public String deleteUser(@PathVariable long id) {
        usersManager.deleteUser(id);
        return "redirect:/users";
    }

    @ModelAttribute("nationalities")
    public Collection<Nationality> populateNationalities() {
        return nationalitiesService.getAll();
    }

    @ModelAttribute("gender")
    public Collection<Gender> populateGender() {
        return genderService.getAll();
    }

    @ModelAttribute("visibilities")
    public Collection<Visibility> populateVisibilities() {
        return visibilityService.getAll();
    }
}
