package com.cloudbees.project.models;

import com.cloudbees.project.dto.BookingRequestDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "receipts")
public class Receipt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "from_station")
    @JsonProperty("from_station")
    private String fromStation;
    @Column(name = "to_station")
    @JsonProperty("to_station")
    private String toStation;
    @Column(name = "amount_paid")
    @JsonProperty("amount_paid")
    private int amountPaid;
    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;
    @OneToOne
    @JoinColumn(name = "seat_number")
    @JsonIgnore
    private Seat seat;

    public Receipt(Seat seat, User user, BookingRequestDto bookingRequestDto) {
        this.fromStation = bookingRequestDto.getFrom();
        this.toStation= bookingRequestDto.getTo();
        this.amountPaid = bookingRequestDto.getPricePaid();
        this.user = user;
        this.seat = seat;
    }
}
