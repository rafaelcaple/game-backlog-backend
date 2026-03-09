package com.rafaelcaple.gamebacklog.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.rafaelcaple.gamebacklog.enums.GameEnums;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="GAMES")

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column (nullable = false)
    private String title;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private GameEnums.GameStatus status;

    @Column(nullable = false)
    private Integer externalId;
    @Column
    private String coverImage;

    @ManyToOne
    @JoinColumn(name="user_id", nullable = false)
    @JsonIgnore
    private User user;

}
