package com.cloudbees.project.dto;

import com.cloudbees.project.constants.Section;
import com.cloudbees.project.models.Seat;
import com.cloudbees.project.models.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class SeatResponseDto {
    @JsonProperty("seat_number")
    private int seatNumber;
    private Section section;
    private User user;

    public SeatResponseDto(Seat seat) {
        this.seatNumber = seat.getNumber();
        this.section = seat.getSection();
        this.user = seat.getUser();
    }
}
