package neurocode.beebetter.controller;

import neurocode.beebetter.dto.AlarmRequestDTO;
import neurocode.beebetter.dto.AlarmResponseDTO;
import neurocode.beebetter.service.AlarmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/alarms")
public class AlarmController {

    @Autowired
    private AlarmService alarmService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<AlarmResponseDTO>> list(@PathVariable Long userId) {
        return ResponseEntity.ok(alarmService.listByUser(userId));
    }

    @PostMapping("/user/{userId}")
    public ResponseEntity<AlarmResponseDTO> create(
            @PathVariable Long userId,
            @RequestBody AlarmRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(alarmService.create(userId, dto));
    }

    @PatchMapping("/{id}/toggle")
    public ResponseEntity<AlarmResponseDTO> toggle(@PathVariable Long id) {
        return ResponseEntity.ok(alarmService.toggleActive(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        alarmService.delete(id);
        return ResponseEntity.noContent().build();
    }
}