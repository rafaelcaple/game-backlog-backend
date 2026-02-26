package com.rafaelcaple.gamebacklog.entity;

import com.rafaelcaple.gamebacklog.enums.GameEnums;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ManyToAny;

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

    @Column(nullable = false, unique = true)
    private Integer rawgId;
    @Column
    private String coverImage;

    @ManyToOne
    @JoinColumn(name="user_id", nullable = false)
    private User user;

}
