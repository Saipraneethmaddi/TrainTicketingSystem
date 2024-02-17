package com.cloudbees.project.dto;

import com.cloudbees.project.constants.TicketStatus;
import com.cloudbees.project.models.Receipt;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BookingResponseDto {

    private String status;
    @JsonProperty("receipt_id")
    private Long receiptId;
    private String from;
    private String to;
    @JsonProperty("price_paid")
    private Integer pricePaid;
    @JsonProperty("user_id")
    private Long userId;
    @JsonProperty("seat_number")
    private Integer seatNumber;
    private String message;

    public BookingResponseDto(Receipt receipt) {
        this.status = receipt.getStatus().getStatus();
        this.receiptId = receipt.getId();
        this.from = receipt.getFromStation();
        this.to = receipt.getToStation();
        this.pricePaid = receipt.getAmountPaid();
        this.userId = receipt.getUser().getId();
        this.seatNumber = receipt.getSeat().getNumber();
    }

    public BookingResponseDto(String message, TicketStatus status) {
        this.status = status.getStatus();
        this.message = message;
    }
}
