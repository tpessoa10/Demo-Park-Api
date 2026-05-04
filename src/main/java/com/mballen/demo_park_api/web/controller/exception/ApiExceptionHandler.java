package com.mballen.demo_park_api.web.controller.exception;

import com.mballen.demo_park_api.exception.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.naming.ConfigurationException;
import java.nio.file.AccessDeniedException;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class ApiExceptionHandler {

    private final MessageSource messageSource;

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorMessage> constraintViolationException(ConstraintViolationException ex, HttpServletRequest request) {
        HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;

        String field = "";
        String message = "";

        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            String path =  violation.getPropertyPath().toString();

            field = path.substring(path.lastIndexOf('.') + 1);
            message = violation.getMessage();
        }

        return ResponseEntity.status(status).body(new ErrorMessage(request, status, field, message));
    }

    @ExceptionHandler({CodigoUniqueViolationException.class})
    public ResponseEntity<ErrorMessage> CodigoUniqueViolationException(CodigoUniqueViolationException ex, HttpServletRequest request) {
        Object[] params = new Object[]{ex.getRecurso(), ex.getCodigo()};
        String message = messageSource.getMessage("excpetion.codigoUniqueViolationException", params, request.getLocale());
        return ResponseEntity.status(HttpStatus.CONFLICT).contentType(MediaType.APPLICATION_JSON)
                .body(new ErrorMessage(request, HttpStatus.UNPROCESSABLE_ENTITY, message));
    }

    @ExceptionHandler(org.springframework.web.bind.MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorMessage> methodArgumentValidException(MethodArgumentNotValidException ex, HttpServletRequest request,
                                                                     BindingResult result) {
        log.error("API ERROR:", ex);
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).contentType(MediaType.APPLICATION_JSON)
                .body(new ErrorMessage(request, HttpStatus.UNPROCESSABLE_ENTITY,
                        messageSource.getMessage("message.invalid.field", null, request.getLocale()),
                        result, messageSource));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorMessage> AccessDeniedException(AccessDeniedException ex, HttpServletRequest request,
                                                                     BindingResult result) {
        log.error("API ERROR:", ex);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).contentType(MediaType.APPLICATION_JSON)
                .body(new ErrorMessage(request, HttpStatus.FORBIDDEN, "Campos inválidos!", result));
    }

    @ExceptionHandler(PasswordInvalidException.class)
    public ResponseEntity<ErrorMessage> passwordInvalidException(
            PasswordInvalidException ex,
            HttpServletRequest request) {

        log.error("API ERROR:", ex);

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ErrorMessage(
                        request,
                        HttpStatus.BAD_REQUEST,
                        ex.getMessage()
                ));
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorMessage> EntityNotFoundException(EntityNotFoundException ex, HttpServletRequest request) {
        log.error("API ERROR:", ex);
        Object[] params = new Object[]{ex.getRecurso(), ex.getCodigo()};
        String message = messageSource.getMessage("excpetion.entityNotFoundExcpetion", params, request.getLocale());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).contentType(MediaType.APPLICATION_JSON)
                .body(new ErrorMessage(request, HttpStatus.NOT_FOUND, message));
    }

    @ExceptionHandler(ReciboCheckinNotFoundExcpetion.class)
    public ResponseEntity<ErrorMessage> reciboCheckinNotFoundExcpetion(ReciboCheckinNotFoundExcpetion ex, HttpServletRequest request) {
        log.error("API ERROR:", ex);
        Object[] params = new Object[]{ex.getRecibo()};
        String message = messageSource.getMessage("excpetion.reciboCheckinNotFoundException", params, request.getLocale());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).contentType(MediaType.APPLICATION_JSON)
                .body(new ErrorMessage(request, HttpStatus.NOT_FOUND, message));
    }

    @ExceptionHandler(VagaDisponivelExcpetion.class)
    public ResponseEntity<ErrorMessage> vagaDisponivelExcpetion(MethodArgumentNotValidException ex, HttpServletRequest request) {
        String message = messageSource.getMessage("excpetion.vagaDisponivelExcpetion", null, request.getLocale());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).contentType(MediaType.APPLICATION_JSON)
                .body(new ErrorMessage(request, HttpStatus.NOT_FOUND, message));
    }

    @ExceptionHandler({UserNameUniqueVioletionException.class, CpfUniqueViolationException.class})
    public ResponseEntity<ErrorMessage> uniqueViolationException(RuntimeException ex, HttpServletRequest request) {
        log.error("API ERROR:", ex);
        return ResponseEntity.status(HttpStatus.CONFLICT).contentType(MediaType.APPLICATION_JSON)
                .body(new ErrorMessage(request, HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorMessage> internalServerErrorExcpetion(Exception ex, HttpServletRequest request) {
        ErrorMessage error = new ErrorMessage(request, HttpStatus.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
        log.error("INTERNAL SERVER ERROR {} {}",error, ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).contentType(MediaType.APPLICATION_JSON)
                .body(error);
    }
}
