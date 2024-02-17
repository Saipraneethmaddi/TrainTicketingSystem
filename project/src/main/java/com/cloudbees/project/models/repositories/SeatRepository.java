package com.cloudbees.project.models.repositories;

import com.cloudbees.project.models.Seat;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface SeatRepository extends CrudRepository<Seat, Integer> {
    @Query("SELECT s FROM Seat s WHERE s.booked=false ORDER BY s.number LIMIT 1")
    Seat findAvailableSeat();
}
