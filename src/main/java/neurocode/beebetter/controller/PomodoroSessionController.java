package neurocode.beebetter.controller;

import neurocode.beebetter.dto.PomodoroSessionRequestDTO;
import neurocode.beebetter.dto.PomodoroSessionResponseDTO;
import neurocode.beebetter.service.PomodoroSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pomodoro")
public class PomodoroSessionController {

    @Autowired
    private PomodoroSessionService pomodoroSessionService;

    @PostMapping("/start")
    public ResponseEntity<PomodoroSessionResponseDTO> start(@RequestBody PomodoroSessionRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(pomodoroSessionService.start(dto));
    }

    @PatchMapping("/{id}/finish")
    public ResponseEntity<PomodoroSessionResponseDTO> finish(@PathVariable Long id) {
        return ResponseEntity.ok(pomodoroSessionService.finish(id));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PomodoroSessionResponseDTO>> listByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(pomodoroSessionService.listByUser(userId));
    }
}
