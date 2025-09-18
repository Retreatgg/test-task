package com.axelor.apps.testtask.exception.handler;

import com.axelor.apps.testtask.exception.TaskNotFoundException;
import com.axelor.apps.testtask.exception.UserNotFoundException;
import com.axelor.apps.testtask.exception.errorDto.AppErrorDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(TaskNotFoundException.class)
    public ResponseEntity<AppErrorDto> handleTaskNotFound(TaskNotFoundException exception) {
        return new ResponseEntity<>(new AppErrorDto(HttpStatus.NOT_FOUND.value(),
                exception.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<AppErrorDto> handleUserNotFound(UserNotFoundException exception) {
        return new ResponseEntity<>(new AppErrorDto(HttpStatus.NOT_FOUND.value(),
                exception.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<AppErrorDto> handleIllegalArgument(IllegalArgumentException exception) {
        return new ResponseEntity<>(new AppErrorDto(HttpStatus.BAD_REQUEST.value(),
                exception.getMessage()), HttpStatus.BAD_REQUEST);
    }

}
