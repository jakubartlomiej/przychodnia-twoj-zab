package com.example.przychodnia.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Data
public class VisitsCalendar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime date;
    private LocalTime start;
    private LocalTime end;
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "patient_id", referencedColumnName = "id")
    private User patient;
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "doctor_id", referencedColumnName = "id")
    private User doctor;

    public VisitsCalendar() {
    }

    public VisitsCalendar(LocalDateTime date, LocalTime start, LocalTime end, User doctor) {
        this.date = date;
        this.start = start;
        this.end = end;
        this.doctor = doctor;
    }
}


