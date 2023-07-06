package com.sojka.sunactivity.security.auth.exception;

import com.google.api.gax.rpc.AlreadyExistsException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.concurrent.ExecutionException;

@ControllerAdvice(basePackages = "com.sojka.sunactivity.security.auth")
@Slf4j
public class SecurityControllerExceptionHandler {

    @ExceptionHandler(value = ExecutionException.class)
    ResponseEntity<ExceptionResponse> handleExecutionException(ExecutionException e) {
        Throwable cause = e.getCause();
        log.debug(cause.getMessage());
        if (cause instanceof AlreadyExistsException alreadyExist) {
            Throwable statusRuntime = alreadyExist.getCause();
            String message = statusRuntime.getMessage();
            String existingEmail = message.substring(message.lastIndexOf("/") + 1);
            return new ResponseEntity<>(new ExceptionResponse(String.format("User %s already exists", existingEmail)),
                    HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(new ExceptionResponse("Cannot register"), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
