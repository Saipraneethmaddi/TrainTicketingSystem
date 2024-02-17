package com.cloudbees.project.config;

import com.cloudbees.project.constants.Section;
import com.cloudbees.project.models.Seat;
import com.cloudbees.project.models.repositories.SeatRepository;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class TrainInitializer {
    private final SeatRepository seatRepository;

    public TrainInitializer(SeatRepository seatRepository) {
        this.seatRepository = seatRepository;
    }

    @PostConstruct
    private void populateSeats() {
        List<Seat> seats = new ArrayList<>();

        int totalCapacity = Section.A.getCapacity() + Section.B.getCapacity();
        Section section;
        for(int i=1; i<=totalCapacity; i++) {
            section = i>Section.A.getCapacity() ? Section.B : Section.A;
            Seat seat = new Seat(); seat.setNumber(i); seat.setSection(section); seat.setBooked(false);
            seats.add(seat);
        }

        seatRepository.saveAll(seats);
        log.info("Seats populated!");
    }
}
