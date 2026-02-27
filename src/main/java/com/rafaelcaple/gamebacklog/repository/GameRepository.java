package com.rafaelcaple.gamebacklog.repository;

import com.rafaelcaple.gamebacklog.entity.Game;
import com.rafaelcaple.gamebacklog.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GameRepository extends JpaRepository<Game,Long> {
    List<Game> findByUserId(Long userId);
    boolean existsByRawgIdAndUser(Integer rawgId, User user);

}
