package com.cloudbees.project.exceptions;

import lombok.Getter;

public class CustomException extends Exception {

    @Getter
    private final ErrorCode errorCode;

    public CustomException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
