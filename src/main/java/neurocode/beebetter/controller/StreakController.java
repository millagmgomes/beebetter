package neurocode.beebetter.controller;

import neurocode.beebetter.dto.StreamPauseRequestDTO;
import neurocode.beebetter.service.StreakService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/streak")
public class StreakController {

    @Autowired private StreakService streakService;

    @PostMapping("/user/{userId}/pause")
    public ResponseEntity<Map<String, String>> pause(
            @PathVariable Long userId,
            @RequestBody StreamPauseRequestDTO dto) {
        String message = streakService.pause(userId, dto);
        return ResponseEntity.ok(Map.of("message", message));
    }

    @PostMapping("/user/{userId}/resume")
    public ResponseEntity<Map<String, String>> resume(@PathVariable Long userId) {
        String message = streakService.resume(userId);
        return ResponseEntity.ok(Map.of("message", message));
    }

    @GetMapping("/user/{userId}/status")
    public ResponseEntity<Map<String, Object>> status(@PathVariable Long userId) {
        boolean paused = streakService.isOnPause(userId);
        return ResponseEntity.ok(Map.of("streakPaused", paused));
    }
}