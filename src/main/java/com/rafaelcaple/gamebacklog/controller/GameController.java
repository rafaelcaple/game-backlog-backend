package com.rafaelcaple.gamebacklog.controller;

import com.rafaelcaple.gamebacklog.entity.Game;
import com.rafaelcaple.gamebacklog.entity.User;
import com.rafaelcaple.gamebacklog.enums.GameEnums;
import com.rafaelcaple.gamebacklog.service.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/games")
@RequiredArgsConstructor
public class GameController {
    private final GameService service;

    @GetMapping("/search")
    public ResponseEntity<Map<String,Object>> searchGames (@RequestParam String query) {
        return ResponseEntity.ok(service.searchGames(query));
    }

    @GetMapping public ResponseEntity<List<Game>> listSaved(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(service.listSaved(user));
    }
        @PostMapping("/save/{rawgId}")
    public ResponseEntity<Game> saveFromRawg (@PathVariable Integer rawgId, @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(service.saveFromRawg(rawgId, user));
    }
    @PatchMapping("/{id}/status")
    public ResponseEntity<Game> updateStatus (@PathVariable Long id, @RequestParam GameEnums.GameStatus status, @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(service.updateStatus(id,status,user));
    }
    @DeleteMapping("/{id}")
    public void delete (@PathVariable Long id, @AuthenticationPrincipal User user) {
        service.delete(id,user);
    }
}
