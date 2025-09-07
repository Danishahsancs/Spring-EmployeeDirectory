package io.zipcoder.persistenceapp.exception;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.*;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@ControllerAdvice
public class RestExceptionHandler {

    @Autowired
    private MessageSource messageSource;

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> handleResourceNotFound(ResourceNotFoundException ex, HttpServletRequest req) {
        ErrorDetail ed = new ErrorDetail();
        ed.setTitle("Resource Not Found");
        ed.setStatus(HttpStatus.NOT_FOUND.value());
        ed.setDetail(ex.getMessage());
        ed.setTimeStamp(new Date().getTime());
        ed.setDeveloperMessage(ex.getClass().getName());
        return new ResponseEntity<>(ed, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationError(MethodArgumentNotValidException manve, HttpServletRequest req) {
        ErrorDetail ed = new ErrorDetail();
        ed.setTitle("Validation Failed");
        ed.setStatus(HttpStatus.BAD_REQUEST.value());
        ed.setDetail("Input validation failed");
        ed.setTimeStamp(new Date().getTime());
        ed.setDeveloperMessage(manve.getClass().getName());
        ed.setErrors(new HashMap<>());

        List<FieldError> fieldErrors = manve.getBindingResult().getFieldErrors();
        for (FieldError fe : fieldErrors) {
            List<ValidationError> list = ed.getErrors().get(fe.getField());
            if (list == null) {
                list = new ArrayList<>();
                ed.getErrors().put(fe.getField(), list);
            }
            ValidationError ve = new ValidationError();
            ve.setCode(fe.getCode());
            ve.setMessage(messageSource.getMessage(fe, null));
            list.add(ve);
        }
        return new ResponseEntity<>(ed, HttpStatus.BAD_REQUEST);
    }
}
