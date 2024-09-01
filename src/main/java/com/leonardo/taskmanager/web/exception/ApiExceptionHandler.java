package com.leonardo.taskmanager.web.exception;

import com.leonardo.taskmanager.exception.EntityNotFoundExecption;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler(EntityNotFoundExecption.class)
    public ResponseEntity<ErrorMessage> EntityNotFoundException(RuntimeException ex,
                                                                HttpServletRequest request
    ){
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ErrorMessage(request, HttpStatus.NOT_FOUND, ex.getMessage()));

    }

    @ExceptionHandler(org.springframework.security.access.AccessDeniedException.class)
    public ResponseEntity<ErrorMessage> accessDeniedException(AccessDeniedException ex,
                                                              HttpServletRequest request
    ){
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ErrorMessage(request, HttpStatus.FORBIDDEN, ex.getMessage()));

    }
}
