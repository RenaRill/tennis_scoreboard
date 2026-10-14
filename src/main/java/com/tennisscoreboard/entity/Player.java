package com.tennisscoreboard.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "players")
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @OneToMany
    private Long id;

    @Column(unique = true)
    private String name;

    public Player(String name) {
        this.name = name;
    }
}
