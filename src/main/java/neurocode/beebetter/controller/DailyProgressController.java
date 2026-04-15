package neurocode.beebetter.controller;

import neurocode.beebetter.dto.DailyProgressResponseDTO;
import neurocode.beebetter.service.DailyProgressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/daily-progress")
public class DailyProgressController {

    @Autowired
    private DailyProgressService dailyProgressService;

    @GetMapping("/today/{userId}")
    public ResponseEntity<DailyProgressResponseDTO> getToday(@PathVariable Long userId) {
        return ResponseEntity.ok(dailyProgressService.getToday(userId));
    }
}
