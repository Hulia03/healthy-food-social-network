package com.telerikacademy.healthy.food.social.network.controllers.rest;

import com.cloudinary.utils.ObjectUtils;
import com.telerikacademy.healthy.food.social.network.exceptions.DuplicateEntityException;
import com.telerikacademy.healthy.food.social.network.exceptions.EntityNotFoundException;
import com.telerikacademy.healthy.food.social.network.exceptions.InvalidRegistrationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;

import static com.telerikacademy.healthy.food.social.network.utils.GlobalConstants.UNKNOWN_ERROR;

@ControllerAdvice(basePackages = "com.telerikacademy.healthy.food.social.network.controllers.rest")
public class ErrorRestController {
    private static final String ERROR_MESSAGE = "error";

    @ExceptionHandler(value = AccessDeniedException.class)
    public ResponseEntity<Map<String, Object>> exceptionAccessDenied(final RuntimeException throwable) {
        String errorMessage = (throwable != null && throwable.getMessage() != null ? throwable.getMessage() : UNKNOWN_ERROR);
        Map<String, Object> body = ObjectUtils.asMap(ERROR_MESSAGE, errorMessage);
        return new ResponseEntity<>(body, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(value = {DuplicateEntityException.class, InvalidRegistrationException.class})
    public ResponseEntity<Map<String, Object>> exceptionDuplicateEntityException(final RuntimeException throwable) {
        String errorMessage = (throwable != null && throwable.getMessage() != null ? throwable.getMessage() : UNKNOWN_ERROR);
        Map<String, Object> body = ObjectUtils.asMap(ERROR_MESSAGE, errorMessage);
        return new ResponseEntity<>(body, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = EntityNotFoundException.class)
    public ResponseEntity<Map<String, Object>> exceptionEntityNotFoundException(final RuntimeException throwable) {
        String errorMessage = (throwable != null && throwable.getMessage() != null ? throwable.getMessage() : UNKNOWN_ERROR);
        Map<String, Object> body = ObjectUtils.asMap(ERROR_MESSAGE, errorMessage);
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<Map<String, Object>> exception(final Exception throwable) {
        String errorMessage = (throwable != null && throwable.getMessage() != null ? throwable.getMessage() : UNKNOWN_ERROR);
        Map<String, Object> body = ObjectUtils.asMap(ERROR_MESSAGE, errorMessage);
        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}