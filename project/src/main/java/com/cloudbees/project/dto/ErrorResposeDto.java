package com.cloudbees.project.dto;

import java.util.Date;

public class ErrorResposeDto {
    private final String status = "failure";
    private final String message;
    private final Long timestamp = new Date().getTime();

    public ErrorResposeDto(String message) {
        this.message = message;
    }
}
