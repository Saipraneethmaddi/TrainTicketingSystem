package com.cloudbees.project.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

public enum ErrorCode {
    USER_ALREADY_EXISTS("A user already exists with same email", HttpStatus.NOT_ACCEPTABLE),
    USER_NOT_FOUND("User not found", HttpStatus.NOT_FOUND),
    USER_ID_MISMATCH("User id mismatch", HttpStatus.BAD_REQUEST),
    USER_EMAIL_MISMATCH("User email mismatch", HttpStatus.BAD_REQUEST),
    EMAIL_NOT_PROVIDED("Email cannot be null", HttpStatus.BAD_REQUEST),
    EMAIL_MISMATCH("Email cannot be modified", HttpStatus.NOT_ACCEPTABLE),
    INVALID_REQUEST_BODY("Invalid request body", HttpStatus.BAD_REQUEST),
    SECTION_DOES_NOT_EXIST("Provided section does not exist", HttpStatus.NOT_FOUND),
    INVALID_USER_ID("Invalid user id", HttpStatus.BAD_REQUEST),
    INVALID_RECEIPT_ID("Invalid receipt id", HttpStatus.BAD_REQUEST),
    RECEIPT_NOT_FOUND("Receipt not found", HttpStatus.NOT_FOUND),
    INVALID_SEAT_NUMBER("Invalid seat number", HttpStatus.BAD_REQUEST),
    SEAT_NOT_FOUND("Seat not found with given number", HttpStatus.NOT_FOUND);


    private final String message;
    private final HttpStatus httpStatusCode;

    ErrorCode(String message, HttpStatus httpStatusCode) {
        this.message = message;
        this.httpStatusCode = httpStatusCode;
    }

    public String getMessage() {
        return this.message;
    }

    public HttpStatusCode getHttpStatusCode() {
        return this.httpStatusCode;
    }
}
