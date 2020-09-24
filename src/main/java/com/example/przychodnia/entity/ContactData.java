package com.example.przychodnia.entity;

import javax.persistence.*;

import lombok.Data;

import java.time.LocalTime;

@Entity
@Data
public class ContactData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String number;
    private String email;
    private String address;
    @Column(name = "open", columnDefinition = "time default '08:00'")
    private LocalTime open = LocalTime.of(8,0);
    @Column(name = "close", columnDefinition = "time default '18:00'")
    private LocalTime close = LocalTime.of(18,0);

}
