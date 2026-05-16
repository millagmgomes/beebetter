package neurocode.beebetter.controller;

import neurocode.beebetter.dto.GoalSummaryDTO;
import neurocode.beebetter.dto.TaskRequestDTO;
import neurocode.beebetter.dto.TaskResponseDTO;
import neurocode.beebetter.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<TaskResponseDTO>> listByUser(
            @PathVariable Long userId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        if (date != null) {
            return ResponseEntity.ok(taskService.listByUserAndDate(userId, date));
        }
        return ResponseEntity.ok(taskService.listByUser(userId));
    }

    @PostMapping
    public ResponseEntity<TaskResponseDTO> create(@RequestBody TaskRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(taskService.create(dto));
    }

    @PatchMapping("/{id}/complete")
    public ResponseEntity<TaskResponseDTO> complete(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.completeTask(id));
    }

    @GetMapping("/user/{userId}/goals")
    public ResponseEntity<GoalSummaryDTO> getGoals(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "TODAY") String period) {
        return ResponseEntity.ok(taskService.getGoalSummary(userId, period));
    }

    @PatchMapping("/{id}/progress")
    public ResponseEntity<TaskResponseDTO> updateProgress(
            @PathVariable Long id,
            @RequestParam Integer increment) {
        return ResponseEntity.ok(taskService.updateMissionProgress(id, increment));
    }

    @GetMapping("/user/{userId}/today")
    public ResponseEntity<List<TaskResponseDTO>> getToday(@PathVariable Long userId) {
        return ResponseEntity.ok(taskService.getTodayTasks(userId));
    }

    @GetMapping("/user/{userId}/in-progress")
    public ResponseEntity<List<TaskResponseDTO>> getInProgress(@PathVariable Long userId) {
        return ResponseEntity.ok(taskService.getInProgressTasks(userId));
    }

    @GetMapping("/user/{userId}/completed")
    public ResponseEntity<List<TaskResponseDTO>> getCompleted(@PathVariable Long userId) {
        return ResponseEntity.ok(taskService.getCompletedTasks(userId));
    }

    @GetMapping("/user/{userId}/missions")
    public ResponseEntity<List<TaskResponseDTO>> getMissions(@PathVariable Long userId) {
        return ResponseEntity.ok(taskService.getMissions(userId));
    }
}
