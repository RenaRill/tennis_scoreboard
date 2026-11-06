package com.tennisscoreboard.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "players")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @Column(unique = true, nullable = false, length = 50)
    private String name;

    public Player(String name) {
        this.name = name;
    }
}
