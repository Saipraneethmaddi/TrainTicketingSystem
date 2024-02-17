package com.cloudbees.project.models.repositories;

import com.cloudbees.project.constants.Section;
import com.cloudbees.project.models.Seat;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SeatRepository extends CrudRepository<Seat, Integer> {
    @Query("SELECT s FROM Seat s WHERE s.booked=false ORDER BY s.number LIMIT 1")
    Seat findAvailableSeat();

    @Query("SELECT COUNT(s) FROM Seat s WHERE s.booked=false")
    Integer countAvailableSeats();

    @Query("SELECT s FROM Seat s WHERE s.booked=true AND s.section=?1")
    List<Seat> getBookedSeatsFromSection(Section section);
}
