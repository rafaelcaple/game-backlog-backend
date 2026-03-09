package com.rafaelcaple.gamebacklog.gamesprovider;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

@Component("IGDB")
public class IgdbProvider implements GameProvider {

    @Value("${igdb.client.id}")
    private String clientId;

    @Value("${igdb.client.secret}")
    private String clientSecret;

    private String cachedToken;
    private long tokenExpiresAt = 0;

    private final WebClient authClient = WebClient.create("https://id.twitch.tv");
    private final WebClient apiClient = WebClient.builder()
            .baseUrl("https://api.igdb.com/v4")
            .build();

    private record IgdbGame(Integer id, String name, IgdbCover cover) {}
    private record IgdbCover(@JsonProperty("image_id") String imageId) {}

    private String getAccessToken() {
        if (cachedToken != null && System.currentTimeMillis() < tokenExpiresAt) {
            return cachedToken;
        }
        Map<?, ?> response = authClient.post()
                .uri(u -> u.path("/oauth2/token")
                        .queryParam("client_id", clientId)
                        .queryParam("client_secret", clientSecret)
                        .queryParam("grant_type", "client_credentials")
                        .build())
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        cachedToken = (String) response.get("access_token");
        int expiresIn = (int) response.get("expires_in");
        tokenExpiresAt = System.currentTimeMillis() + (expiresIn - 300L) * 1000;
        return cachedToken;
    }

    private WebClient.RequestBodySpec post(String path) {
        return apiClient.post()
                .uri(path)
                .header("Client-ID", clientId)
                .header("Authorization", "Bearer " + getAccessToken())
                .header("Content-Type", "text/plain");
    }

    private GameSearchResult toResult(IgdbGame g) {
        String coverUrl = g.cover() != null
                ? "https://images.igdb.com/igdb/image/upload/t_cover_big/" + g.cover().imageId() + ".jpg"
                : null;
        return new GameSearchResult(g.id(), g.name(), coverUrl);
    }

    @Override
    public List<GameSearchResult> searchGames(String query) {
        String body = "fields id,name,cover.image_id; search \"" + query + "\"; limit 20; where version_parent = null;";
        return post("/games")
                .bodyValue(body)
                .retrieve()
                .bodyToFlux(IgdbGame.class)
                .map(this::toResult)
                .collectList()
                .block();
    }

    @Override
    public GameSearchResult getGameById(Integer id) {
        String body = "fields id,name,cover.image_id; where id = " + id + ";";
        return post("/games")
                .bodyValue(body)
                .retrieve()
                .bodyToFlux(IgdbGame.class)
                .next()
                .map(this::toResult)
                .block();
    }
}