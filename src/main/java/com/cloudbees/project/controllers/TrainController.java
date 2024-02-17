package com.cloudbees.project.controllers;

import com.cloudbees.project.dto.BookingRequestDto;
import com.cloudbees.project.exceptions.CustomException;
import com.cloudbees.project.services.TrainService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class TrainController {
    private final TrainService trainService;

    public TrainController(TrainService trainService) {
        this.trainService = trainService;
    }

    @PostMapping("/train/book_ticket")
    public ResponseEntity<?> bookTicket(@RequestBody BookingRequestDto bookingRequestDto) {
        try {
            return new ResponseEntity<>(trainService.bookTicket(bookingRequestDto), HttpStatus.OK);
        } catch (CustomException e) {
            return new ResponseEntity<>(e.getMessage(), e.getErrorCode().getHttpStatusCode());
        }
    }

    @GetMapping("/train/tickets")
    public ResponseEntity<?> getTicketDetails(@RequestParam(name = "user_id") String userId) {
        try {
            return new ResponseEntity<>(trainService.getTicketDetailsForUser(userId), HttpStatus.OK);
        } catch (CustomException e) {
            return new ResponseEntity<>(e.getMessage(), e.getErrorCode().getHttpStatusCode());
        }
    }

    @GetMapping("/train/tickets/{receiptId}")
    public ResponseEntity<?> getTicketDetailsForReceipt(@PathVariable String receiptId) {
        try {
            return new ResponseEntity<>(trainService.getTicketDetailsFromReceipt(receiptId), HttpStatus.OK);
        } catch (CustomException e) {
            return new ResponseEntity<>(e.getMessage(), e.getErrorCode().getHttpStatusCode());
        }
    }

    @GetMapping("train/seats")
    public ResponseEntity<?> getBookedSeatsFromSection(@RequestParam(name = "section") String section) {
        try {
            return new ResponseEntity<>(trainService.getBookedSeatsFromSection(section), HttpStatus.OK);
        } catch (CustomException e) {
            return new ResponseEntity<>(e.getMessage(), e.getErrorCode().getHttpStatusCode());
        }
    }

    @DeleteMapping("train/tickets/{receiptId}")
    public ResponseEntity<?> cancelTicket(@PathVariable String receiptId) {
        try {
            return new ResponseEntity<>(trainService.cancelTicket(receiptId), HttpStatus.OK);
        } catch (CustomException e) {
            return new ResponseEntity<>(e.getMessage(), e.getErrorCode().getHttpStatusCode());
        }
    }

    //When seat number is provided, we will try to provide that seat. If not, we will provide first available seat
    @PutMapping("train/tickets/{receiptId}")
    public ResponseEntity<?> modifySeat(@RequestParam(name = "seat_number", required = false) String seatNumber,
                                        @PathVariable String receiptId) {
        try {
            return new ResponseEntity<>(trainService.modifySeat(receiptId, seatNumber), HttpStatus.OK);
        } catch (CustomException e) {
            return new ResponseEntity<>(e.getMessage(), e.getErrorCode().getHttpStatusCode());
        }
    }
}
