package com.rafaelcaple.video_game_manager.repository;

import com.rafaelcaple.video_game_manager.entity.Game;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameRepository extends JpaRepository<Game,Long> {}
