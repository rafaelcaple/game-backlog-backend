package com.rafaelcaple.gamebacklog.gamesprovider;

import java.util.List;

public interface GameProvider {
    List <GameSearchResult> searchGames(String query);
    GameSearchResult getGameById(Integer id);
}

