package com.ferraro.JobPlatform.exceptions;

import com.ferraro.JobPlatform.model.document.ConfirmationToken;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.DuplicateFormatFlagsException;
import java.util.HashMap;
import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MailNotSentException.class)
    public ResponseEntity<String> mailNotSent(MailNotSentException ex){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<HashMap<String, String>> validation(MethodArgumentNotValidException ex){
        HashMap<String, String> map = new HashMap<>();
        List<FieldError> errors = ex.getFieldErrors();
        for(FieldError error: errors){
            map.put(error.getField(), error.getDefaultMessage());
        }
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(map);
    }

    @ExceptionHandler({ConfirmationTokenNotFoundException.class, UserNotFoundException.class})
    public ResponseEntity<String> notFoundExceptions(RuntimeException ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ex.getMessage());
    }

    @ExceptionHandler(DuplicateRegistrationException.class)
    public ResponseEntity<String> registrationException(DuplicateRegistrationException ex){
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ex.getMessage());
    }

    @ExceptionHandler(UserNotEnabledException.class)
    public ResponseEntity<String> notEnabledException(UserNotEnabledException ex){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ex.getMessage());
    }
}
