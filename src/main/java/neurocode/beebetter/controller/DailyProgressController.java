package neurocode.beebetter.controller;

import neurocode.beebetter.dto.DailyProgressResponseDTO;
import neurocode.beebetter.service.DailyProgressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/daily-progress")
public class DailyProgressController {

    @Autowired
    private DailyProgressService dailyProgressService;

    @GetMapping("/today/{userId}")
    public ResponseEntity<DailyProgressResponseDTO> getToday(@PathVariable Long userId) {
        return ResponseEntity.ok(dailyProgressService.getToday(userId));
    }

    @GetMapping("/history/{userId}")
    public ResponseEntity<List<DailyProgressResponseDTO>> getHistory(
            @PathVariable Long userId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
        return ResponseEntity.ok(dailyProgressService.getHistory(userId, start, end));
    }
}