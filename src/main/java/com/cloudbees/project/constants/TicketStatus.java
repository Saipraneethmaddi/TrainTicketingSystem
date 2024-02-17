package com.cloudbees.project.constants;

public enum TicketStatus {
    BOOKED("Booked"),
    NOT_BOOKED("Not booked"),
    CANCELLED("Cancelled");

    private final String status;

    TicketStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return this.status;
    }
}
