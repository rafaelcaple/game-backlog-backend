package com.rafaelcaple.gamebacklog.repository;

import com.rafaelcaple.gamebacklog.entity.Game;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameRepository extends JpaRepository<Game,Long> {
    boolean existsByRawgId(Integer rawgId);
}
