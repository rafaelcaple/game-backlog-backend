package com.rafaelcaple.video_game_manager.service;

import com.rafaelcaple.video_game_manager.entity.Game;
import com.rafaelcaple.video_game_manager.enums.GameEnums;
import com.rafaelcaple.video_game_manager.rawg.RawgClient;
import com.rafaelcaple.video_game_manager.repository.GameRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class GameService {
    private final GameRepository repo;
    private final RawgClient rawgClient;

    public Map<String,Object> searchGames (String query) {
        return rawgClient.searchGames(query);
    }

    public Game saveFromRawg(Integer rawgId) {
        Map<String,Object> data = rawgClient.getGameById(rawgId);
        Game game = new Game();
        game.setTitle((String) data.get("name"));
        game.setStatus(GameEnums.GameStatus.PLAYING);
        game.setRawgId(rawgId);
        game.setCoverImage((String) data.get("background_image"));
        return repo.save(game);
    }

    public List<Game> listSaved() {
        return repo.findAll();
    }

    public Game updateStatus(Long id, GameEnums.GameStatus status) {
        Game game = repo.findById(id).orElseThrow();
        game.setStatus(status);
        return repo.save(game);
    }

    public void delete (Long id) {
        Game game = repo.findById(id).orElseThrow();
        repo.delete(game);
    }

}
