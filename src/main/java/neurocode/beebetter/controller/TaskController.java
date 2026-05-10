package neurocode.beebetter.controller;

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
}
