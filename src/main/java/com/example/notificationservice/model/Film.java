package com.example.notificationservice.model;

import lombok.Data;

import jakarta.persistence.*;

@Entity
@Table(name = "film")
@Data
public class Film {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}