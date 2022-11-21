package com.telerikacademy.healthy.food.social.network.controllers.mvc;

import com.telerikacademy.healthy.food.social.network.exceptions.DuplicateEntityException;
import com.telerikacademy.healthy.food.social.network.exceptions.EntityNotFoundException;
import com.telerikacademy.healthy.food.social.network.exceptions.InvalidRegistrationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import static com.telerikacademy.healthy.food.social.network.utils.GlobalConstants.UNKNOWN_ERROR;

@ControllerAdvice(basePackages = "com.telerikacademy.healthy.food.social.network.controllers.mvc")
public class ErrorMvcController {
    @ExceptionHandler(value = AccessDeniedException.class)
    @ResponseStatus(code = HttpStatus.UNAUTHORIZED)
    public String exceptionAccessDenied() {
        return "access-denied";
    }

    @ExceptionHandler(value = {DuplicateEntityException.class, InvalidRegistrationException.class})
    @ResponseStatus(code = HttpStatus.CONFLICT)
    public String exceptionDuplicateEntityException(final RuntimeException throwable, final Model model) {
        String errorMessage = (throwable != null && throwable.getMessage() != null ? throwable.getMessage() : UNKNOWN_ERROR);
        model.addAttribute("message", errorMessage);
        return "error";
    }

    @ExceptionHandler(value = EntityNotFoundException.class)
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    public String exceptionEntityNotFoundException(final RuntimeException throwable, final Model model) {
        String errorMessage = (throwable != null && throwable.getMessage() != null ? throwable.getMessage() : UNKNOWN_ERROR);
        model.addAttribute("message", errorMessage);
        return "error";
    }

    @ExceptionHandler(value = Exception.class)
    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    public String exception(final Exception throwable, final Model model) {
        String errorMessage = (throwable != null && throwable.getMessage() != null ? throwable.getMessage() : UNKNOWN_ERROR);
        model.addAttribute("message", errorMessage);
        model.addAttribute("exception", String.valueOf(throwable));
        return "error";
    }
}