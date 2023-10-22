package net.kravuar.tinkofffootball.application.web;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import net.kravuar.tinkofffootball.domain.model.exceptions.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class ExceptionHandlerService {

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public String handle(IllegalArgumentException exception) {
        return exception.getMessage();
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public String handle(ResourceNotFoundException exception) {
        return exception.getMessage();
    }

    @ExceptionHandler(value = {AccessDeniedException.class, BadCredentialsException.class})
    @ResponseStatus(code = HttpStatus.FORBIDDEN)
    public String handle(Exception exception) {
        return exception.getMessage();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public List<String> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        return ex.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> String.format("%s: %s", fieldError.getField(), fieldError.getDefaultMessage()))
                .toList();
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public List<String> handleConstraintViolation(ConstraintViolationException ex) {
        return ex.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .toList();
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleOther() {
        return "Что-то плохое случилось на сервере.";
    }
}
