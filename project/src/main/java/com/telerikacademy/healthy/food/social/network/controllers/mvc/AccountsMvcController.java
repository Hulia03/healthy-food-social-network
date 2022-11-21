package com.telerikacademy.healthy.food.social.network.controllers.mvc;

import com.telerikacademy.healthy.food.social.network.models.*;
import com.telerikacademy.healthy.food.social.network.models.dtos.PasswordUserDto;
import com.telerikacademy.healthy.food.social.network.models.dtos.mappers.UserDetailsMapper;
import com.telerikacademy.healthy.food.social.network.models.dtos.users.UserRegisterDto;
import com.telerikacademy.healthy.food.social.network.services.contracts.GenderService;
import com.telerikacademy.healthy.food.social.network.services.contracts.NationalitiesService;
import com.telerikacademy.healthy.food.social.network.services.contracts.VisibilitiesService;
import com.telerikacademy.healthy.food.social.network.services.managers.contracts.AccountsManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

import static com.telerikacademy.healthy.food.social.network.utils.GlobalConstants.*;

@Controller
public class AccountsMvcController {
    private final AccountsManager accountsManager;
    private final PasswordEncoder passwordEncoder;
    private final NationalitiesService nationalitiesService;
    private final GenderService genderService;
    private final VisibilitiesService visibilityService;
    private final UserDetailsMapper userDetailsMapper;

    @Autowired
    public AccountsMvcController(AccountsManager accountsManager, PasswordEncoder passwordEncoder, NationalitiesService nationalitiesService, GenderService genderService, VisibilitiesService visibilityService, UserDetailsMapper userDetailsMapper) {
        this.accountsManager = accountsManager;
        this.passwordEncoder = passwordEncoder;
        this.nationalitiesService = nationalitiesService;
        this.genderService = genderService;
        this.visibilityService = visibilityService;
        this.userDetailsMapper = userDetailsMapper;
    }

    @GetMapping("/login")
    public String showLogin() {
        return "login";
    }

    @GetMapping("/access-denied")
    public String showAccessDenied() {
        return "access-denied";
    }

    @GetMapping("/register")
    public String showRegisterPage(final Model model) {
        model.addAttribute("user", new UserRegisterDto());
        return "users/register/register";
    }

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("user") UserRegisterDto dto,
                               BindingResult errors,
                               final Model model) {
        if (errors.hasErrors()) {
            return "users/register/register";
        }

        if (dto.getPicture().isEmpty()) {
            model.addAttribute("error", FILE_REQUIRED_MESSAGE_ERROR);
            return "users/register/register";
        }

        List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList("ROLE_USER");
        org.springframework.security.core.userdetails.User newUser =
                new org.springframework.security.core.userdetails.User(
                        dto.getUsername(),
                        passwordEncoder.encode(dto.getPassword()),
                        false, true, true, true,
                        authorities);
        UserDetails userDetails = userDetailsMapper.toUser(dto);
        accountsManager.register(userDetails, newUser);
        ConfirmationToken token = accountsManager.createConfirmationToken(userDetails.getEmail());
        accountsManager.sendConfirmationEmail(token, COMPLETE_REGISTRATION, "register-confirmation");
        return "users/register/register-confirmation";
    }

    @GetMapping("/register_confirmation")
    public String showRegisterConfirmation() {
        return "users/register/register-confirmation";
    }

    @GetMapping("/confirm_account")
    public String showConfirmUserAccount(@RequestParam("token") String confirmationToken) {
        accountsManager.confirmRegistration(confirmationToken);
        return "login";
    }

    @GetMapping("/forgot_password")
    public String showForgotPassword() {
        return "users/password/forgot-password";
    }

    @PostMapping("/forgot_password")
    public String showForgotPassword(String username, final Model model) {
        if (!accountsManager.userExists(username)) {
            model.addAttribute("error", USER_WITH_THIS_EMAIL_DOES_NOT_EXIST);
            return "users/password/forgot-password";
        }

        ConfirmationToken token = accountsManager.forgotPassword(username);
        accountsManager.sendConfirmationEmail(token, CONFIRM_RESET_PASSWORD, "password-confirmation");
        return "users/password/email-sent";
    }

    @GetMapping("/password_confirmation")
    public String showConfirmPasswordReset(@RequestParam("token") String confirmationToken, final Model model) {
        String username = accountsManager.getUsernameByToken(confirmationToken);
        PasswordUserDto userDto = new PasswordUserDto();
        userDto.setUsername(username);
        model.addAttribute("user", userDto);
        return "users/password/reset-password";
    }

    @PostMapping("/password_confirmation")
    public String passwordChange(@Valid @ModelAttribute("user") PasswordUserDto userDto, BindingResult errors) {
        if (errors.hasErrors()) {
            return "users/password/reset-password";
        }

        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
        accountsManager.changePassword(userDto);
        return "redirect:/login";
    }

    @GetMapping(USERS_URL + "/password")
    public String showChangePasswordForm(final Model model, final Authentication auth) {
        PasswordUserDto dto = new PasswordUserDto();
        dto.setUsername(auth.getName());
        model.addAttribute("user", dto);
        return "users/password/change-password";
    }

    @PostMapping(USERS_URL + "/password")
    public String updateChangePasswordForm(@Valid @ModelAttribute("user") PasswordUserDto user,
                                           BindingResult errors,
                                           final Model model) {
        if (errors.hasErrors()) {
            return "users/password/change-password";
        }

        if (!accountsManager.userExists(user.getUsername())) {
            model.addAttribute("error", USER_WITH_THIS_EMAIL_DOES_NOT_EXIST);
        }

        String oldPassword = SecurityContextHolder.getContext()
                .getAuthentication().getCredentials().toString();

        if (!user.getOldPassword().equals(oldPassword)) {
            model.addAttribute("error", USER_S_OLD_PASSWORD_DOESN_T_MATCH);
            return "users/password/change-password";
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        accountsManager.changePassword(user);

        return "redirect:/home";
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
