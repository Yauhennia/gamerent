package ru.aston.gamerent.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class ActionType {
    @Id
    Long id;
}