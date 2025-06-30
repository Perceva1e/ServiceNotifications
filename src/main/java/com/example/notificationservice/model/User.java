package com.example.notificationservice.model;

import lombok.Data;

import jakarta.persistence.*;

@Entity
@Table(name = "app_user")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}