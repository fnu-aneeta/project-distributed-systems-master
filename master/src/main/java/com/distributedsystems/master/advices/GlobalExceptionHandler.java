package com.distributedsystems.master.advices;

import com.distributedsystems.master.exceptions.UserAlreadyExistsException;
import com.distributedsystems.master.model.ErrorResource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

        final BindingResult bindingResult = ex.getBindingResult();
        final List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        final Set<String> validationErrors = fieldErrors.stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toSet());
        final ErrorResource error = new ErrorResource(HttpStatus.BAD_REQUEST, "Validation Error", new ArrayList<>(validationErrors));
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<Object> userAlreadyExists(UserAlreadyExistsException exception) {
        log.error(exception.getMessage());
        ResponseEntity.BodyBuilder responseBuilder = ResponseEntity.status(CONFLICT);
        return responseBuilder.body(ErrorResource.builder()
                .message(exception.getMessage())
                .code(CONFLICT.value())
                .status(CONFLICT)
                .errorDetails(List.of(exception.getMessage()))
                .build());
    }
}
