package com.shoponlineback;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.List;

@ControllerAdvice
@EnableWebMvc
public class GlobalControllerAdvice{

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidationException(MethodArgumentNotValidException exception){
        List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();
        String message = "Field " +
                fieldErrors.get(0).getField() +
                ": " +
                fieldErrors.get(0).getDefaultMessage();
        return ResponseEntity.badRequest().body(message);
    }
}
