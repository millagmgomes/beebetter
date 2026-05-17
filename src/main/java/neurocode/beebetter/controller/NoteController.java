package neurocode.beebetter.controller;

import neurocode.beebetter.dto.NoteRequestDTO;
import neurocode.beebetter.dto.NoteResponseDTO;
import neurocode.beebetter.service.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notes")
public class NoteController {

    @Autowired private NoteService noteService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<NoteResponseDTO>> listByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(noteService.listByUser(userId));
    }

    @PostMapping
    public ResponseEntity<NoteResponseDTO> create(@RequestBody NoteRequestDTO dto) {
        System.out.println("DTO recebido: " + dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(noteService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<NoteResponseDTO> update(
            @PathVariable Long id,
            @RequestBody NoteRequestDTO dto) {
        return ResponseEntity.ok(noteService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        noteService.delete(id);
        return ResponseEntity.noContent().build();
    }
}