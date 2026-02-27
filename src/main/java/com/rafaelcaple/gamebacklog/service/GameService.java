package com.rafaelcaple.gamebacklog.service;

import com.rafaelcaple.gamebacklog.entity.Game;
import com.rafaelcaple.gamebacklog.entity.User;
import com.rafaelcaple.gamebacklog.enums.GameEnums;
import com.rafaelcaple.gamebacklog.rawg.RawgClient;
import com.rafaelcaple.gamebacklog.repository.GameRepository;
import com.rafaelcaple.gamebacklog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class GameService {
    private final GameRepository repo;
    private final UserRepository userRepository;
    private final RawgClient rawgClient;

    public Map<String,Object> searchGames (String query) {
        return rawgClient.searchGames(query);
    }

    public Game saveFromRawg(Integer rawgId, User user) {
        User persistedUser = userRepository.findByUsername(
                user.getUsername()) .orElseThrow(() -> new RuntimeException("User not found"));
        if (repo.existsByRawgIdAndUser(rawgId, persistedUser)) {
            throw new RuntimeException("Game already on your list");
        }
        Map<String,Object> data = rawgClient.getGameById(rawgId);
        Game game = new Game();
        game.setTitle((String) data.get("name"));
        game.setStatus(GameEnums.GameStatus.PLAYING);
        game.setRawgId(rawgId);
        game.setCoverImage((String) data.get("background_image"));
        game.setUser(persistedUser);

        return repo.save(game);
    }

    public List<Game> listSaved(User user) {
        User persistedUser = userRepository.findByUsername(user.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        return repo.findByUserId(persistedUser.getId());
    }

    public Game updateStatus(Long id, GameEnums.GameStatus status, User user) {
        Game game = repo.findById(id).orElseThrow();
        if (!game.getUser().equals(user)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        game.setStatus(status);
        return repo.save(game);
    }

    public void delete (Long id, User user) {
        Game game = repo.findById(id).orElseThrow();
        if (!game.getUser().equals(user)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        repo.delete(game);
    }

}
