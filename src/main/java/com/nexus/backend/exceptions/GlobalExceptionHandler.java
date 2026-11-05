package com.nexus.backend.exceptions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.context.MessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.validation.method.ParameterValidationResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.exc.InvalidFormatException;


@RestControllerAdvice 
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {

        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problem.setTitle("Bad Request");

        InvalidFormatException invalidFormat = buscarInvalidFormat(ex);

        if (invalidFormat != null && invalidFormat.getTargetType() != null
                && invalidFormat.getTargetType().isEnum()) {

            String field = invalidFormat.getPath().stream()
                    .map(JacksonException.Reference::getPropertyName)
                    .filter(Objects::nonNull)
                    .reduce((first, second) -> second)
                    .orElse("unknown");

            String rejectedValue = String.valueOf(invalidFormat.getValue());

            List<String> allowedValues = Arrays.stream(invalidFormat.getTargetType().getEnumConstants())
                    .map(value -> ((Enum<?>) value).name())
                    .toList();

            problem.setDetail("The provided value is invalid for this field.");
            problem.setProperty("field", field);
            problem.setProperty("rejectedValue", rejectedValue);
            problem.setProperty("allowedValues", allowedValues);

        } else {
            problem.setDetail("The request body is invalid.");
        }

        return handleExceptionInternal(ex, problem, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {

        List<Map<String, Object>> errors = new ArrayList<>();

        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            errors.add(criarErro(fieldError.getField(), fieldError.getRejectedValue(),
                    fieldError.getDefaultMessage()));
        }

        return handleExceptionInternal(ex, criarProblemaValidacao(errors), headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleHandlerMethodValidationException(
            HandlerMethodValidationException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {

        List<Map<String, Object>> errors = new ArrayList<>();

        for (ParameterValidationResult result : ex.getParameterValidationResults()) {
            for (MessageSourceResolvable error : result.getResolvableErrors()) {
                if (error instanceof FieldError fieldError) {
                    errors.add(criarErro(fieldError.getField(), fieldError.getRejectedValue(),
                            fieldError.getDefaultMessage()));
                } else {
                    errors.add(criarErro(result.getMethodParameter().getParameterName(), result.getArgument(),
                            error.getDefaultMessage()));
                }
            }
        }

        return handleExceptionInternal(ex, criarProblemaValidacao(errors), headers, status, request);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ProblemDetail handleConstraintViolation(ConstraintViolationException ex) {

        List<Map<String, Object>> errors = new ArrayList<>();

        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            errors.add(criarErro(violation.getPropertyPath().toString(), violation.getInvalidValue(),
                    violation.getMessage()));
        }

        return criarProblemaValidacao(errors);
    }

    private ProblemDetail criarProblemaValidacao(List<Map<String, Object>> errors) {
        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problem.setTitle("Bad Request");
        problem.setDetail("One or more fields are invalid.");
        problem.setProperty("errors", errors);
        return problem;
    }

    private Map<String, Object> criarErro(String field, Object rejectedValue, String message) {
        Map<String, Object> error = new LinkedHashMap<>();
        error.put("field", field);
        error.put("rejectedValue", String.valueOf(rejectedValue));
        error.put("message", message);
        return error;
    }

    private InvalidFormatException buscarInvalidFormat(Throwable ex) {
        Throwable causa = ex;
        while (causa != null) {
            if (causa instanceof InvalidFormatException invalidFormat) {
                return invalidFormat;
            }
            causa = causa.getCause();
        }
        return null;
    }

}


