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
    public ResponseEntity<?> bookTicket(@RequestBody BookingRequestDto bookingRequestDto) throws CustomException {
        return new ResponseEntity<>(trainService.bookTicket(bookingRequestDto), HttpStatus.OK);
    }

    @GetMapping("/train/tickets")
    public ResponseEntity<?> getTicketDetails(@RequestParam(name = "user_id") Long userId) throws CustomException{
        return new ResponseEntity<>(trainService.getTicketDetailsForUser(userId), HttpStatus.OK);
    }

}
