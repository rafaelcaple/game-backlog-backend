package com.rafaelcaple.video_game_manager.rawg;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Component
public class RawgClient {

    private final WebClient webClient;

    @Value("${rawg.api.key}")
    private String apiKey;

    public RawgClient (){
        this.webClient = WebClient.builder()
                .baseUrl("https://api.rawg.io/api")
                .build();
    }

    public Map<String, Object> searchGames(String query) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/games")
                        .queryParam("key",apiKey)
                        .queryParam("search",query)
                        .build())
                .retrieve()
                .bodyToMono(Map.class)
                .block();
    }

    public Map<String, Object> getGameById (Integer rawgId) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/games/"+ rawgId)
                        .queryParam("key",apiKey)
                        .build()
                )
                .retrieve()
                .bodyToMono(Map.class)
                .block();
    }
}
