package com.cloudbees.project.exceptions;

import com.cloudbees.project.dto.ErrorResposeDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = CustomException.class)
    public ResponseEntity<?> handleCustomException(CustomException customException, WebRequest webRequest) {
        return new ResponseEntity<>(new ErrorResposeDto(customException.getMessage()),
                                    customException.getErrorCode().getHttpStatusCode()
        );
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<?> handleAllException(Exception exception, WebRequest webRequest) {
        return new ResponseEntity<>(new ErrorResposeDto(exception.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
