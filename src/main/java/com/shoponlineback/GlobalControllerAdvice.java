package com.shoponlineback;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@ControllerAdvice
@EnableWebMvc
public class GlobalControllerAdvice{

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception){
        List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();
        String message = "Field " +
                fieldErrors.get(0).getField() +
                ": " +
                fieldErrors.get(0).getDefaultMessage();
        return ResponseEntity.badRequest().body(message);
    }
    @ExceptionHandler(ConstraintViolationException.class)
    public  ResponseEntity<String> handleConstraintViolationException(ConstraintViolationException exception){
        Set<ConstraintViolation<?>> constraintViolations = exception.getConstraintViolations();
        List<String> messages = new ArrayList<>();
        for (ConstraintViolation<?> constraintViolation : constraintViolations) {
            String constraintViolationMessage = constraintViolation.getMessage();
            messages.add(constraintViolationMessage);
        }
        return ResponseEntity.badRequest().body(messages.toString());
    }

}
