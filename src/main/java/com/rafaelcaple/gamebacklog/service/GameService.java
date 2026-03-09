package com.rafaelcaple.gamebacklog.service;

import com.rafaelcaple.gamebacklog.entity.Game;
import com.rafaelcaple.gamebacklog.entity.User;
import com.rafaelcaple.gamebacklog.enums.GameEnums;
import com.rafaelcaple.gamebacklog.gamesprovider.GameProvider;
import com.rafaelcaple.gamebacklog.gamesprovider.GameSearchResult;
import com.rafaelcaple.gamebacklog.repository.GameRepository;
import com.rafaelcaple.gamebacklog.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@Service
public class GameService {
    private final GameRepository repo;
    private final UserRepository userRepository;
    private final GameProvider gameProvider;

    public GameService(
            GameRepository repo,
            UserRepository userRepository,
            @Value("${game.provider}") String provider,
            Map<String, GameProvider> providers) {
        this.repo = repo;
        this.userRepository = userRepository;
        this.gameProvider = providers.get(provider);
    }

    public List<GameSearchResult> searchGames(String query) {
        return gameProvider.searchGames(query);
    }

    public Game saveGame(Integer externalId, User user) {
        User persistedUser = userRepository.findByUsername(user.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (repo.existsByExternalIdAndUser(externalId, persistedUser)) {
            throw new RuntimeException("Game already on your list");
        }
        GameSearchResult data = gameProvider.getGameById(externalId);
        Game game = new Game();
        game.setTitle(data.name());
        game.setStatus(GameEnums.GameStatus.PLAYING);
        game.setExternalId(externalId);
        game.setCoverImage(data.coverUrl());
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

    public void delete(Long id, User user) {
        Game game = repo.findById(id).orElseThrow();
        if (!game.getUser().equals(user)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        repo.delete(game);
    }
}