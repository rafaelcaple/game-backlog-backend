package com.rafaelcaple.gamebacklog.gamesprovider;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Component("RAWG")
public class RawgProvider implements GameProvider {

    private final WebClient webClient;

    @Value("${rawg.api.key}")
    private String apiKey;

    private record RawgGame(Integer id, String name, @JsonProperty("background_image") String backgroundImage) {}
    private record RawgResponse(List<RawgGame> results) {}

    public RawgProvider() {
        this.webClient = WebClient.builder()
                .baseUrl("https://api.rawg.io/api")
                .build();
    }

    @Override
    public List<GameSearchResult> searchGames(String query) {
        RawgResponse response = webClient.get()
                .uri(u -> u.path("/games")
                        .queryParam("key", apiKey)
                        .queryParam("search", query)
                        .queryParam("exclude_additions", true)
                        .queryParam("ordering", "-relevance")
                        .build())
                .retrieve()
                .bodyToMono(RawgResponse.class)
                .block();

        return response.results().stream()
                .map(g -> new GameSearchResult(g.id(), g.name(), g.backgroundImage()))
                .toList();
    }

    @Override
    public GameSearchResult getGameById(Integer id) {
        RawgGame game = webClient.get()
                .uri(u -> u.path("/games/" + id)
                        .queryParam("key", apiKey)
                        .build())
                .retrieve()
                .bodyToMono(RawgGame.class)
                .block();

        return new GameSearchResult(game.id(), game.name(), game.backgroundImage());
    }
}