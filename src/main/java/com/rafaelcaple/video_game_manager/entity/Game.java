package com.rafaelcaple.video_game_manager.entity;

import com.rafaelcaple.video_game_manager.enums.GameEnums;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="GAMES")

@Data
@NoArgsConstructor
@AllArgsConstructor

public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column (nullable = false)
    private String title;
    @Column(nullable = false, unique = true)
    private String rawgId;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private GameEnums.GameStatus status;
}
