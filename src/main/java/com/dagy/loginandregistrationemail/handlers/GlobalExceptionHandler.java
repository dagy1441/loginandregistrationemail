package com.dagy.loginandregistrationemail.handlers;

import com.dagy.loginandregistrationemail.exceptions.EntityAllReadyExistException;
import com.dagy.loginandregistrationemail.exceptions.IncorrectPasswordException;
import com.dagy.loginandregistrationemail.exceptions.ObjectNotValidException;
import com.dagy.loginandregistrationemail.utilities.helpers.ApiDataResponse;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

import static java.time.LocalDateTime.now;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ApiDataResponse> handleException(IllegalStateException exception){

        return ResponseEntity.ok(
                ApiDataResponse.builder()
                        .time(now())
                        .message("Illegal State Exception")
                        .httpStatus(HttpStatus.BAD_REQUEST)
                        .statusCode(HttpStatus.BAD_REQUEST.value())
                        .error(Map.of("errors", ResponseEntity.badRequest().body(exception.getMessage())))
                        .build()
        );
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<?> handleException(){
        return ResponseEntity
                .notFound()
                .build();
    }

    @ExceptionHandler(ObjectNotValidException.class)
    public ResponseEntity<?> handleException(ObjectNotValidException exception){

        return  ResponseEntity
                .badRequest()
                .body(ApiDataResponse.builder()
                        .time(now())
                        .message("Illegal State Exception")
                        .httpStatus(HttpStatus.BAD_REQUEST)
                        .statusCode(HttpStatus.BAD_REQUEST.value())
                        .error(Map.of("errors", exception.getErrorMessages()))
                        .build());


//        return ResponseEntity
//                .badRequest()
//                .body(exception.getErrorMessages());
    }

    @ExceptionHandler(EntityAllReadyExistException.class)
    public ResponseEntity<?> handleException(EntityAllReadyExistException exception){

        return  ResponseEntity
                .badRequest()
                .body(ApiDataResponse.builder()
                        .time(now())
                        .message(exception.getMessage())
                        .httpStatus(HttpStatus.CONFLICT)
                        .statusCode(HttpStatus.CONFLICT.value())
                        .build());

    }

    @ExceptionHandler(IncorrectPasswordException.class)
    public ResponseEntity<?> handleException(IncorrectPasswordException exception){

        return  ResponseEntity
                .badRequest()
                .body(ApiDataResponse.builder()
                        .time(now())
                        .message(exception.getMessage())
                        .httpStatus(HttpStatus.CONFLICT)
                        .statusCode(HttpStatus.CONFLICT.value())
                        .build());

    }



}
