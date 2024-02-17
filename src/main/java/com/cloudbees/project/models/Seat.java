package com.cloudbees.project.models;

import com.cloudbees.project.constants.Section;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "seats")
public class Seat {

    @Id
    @Column(name = "number")
    private int number;
    @Column(name = "section")
    private Section section;
    @Column(name = "booked")
    private boolean booked;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
