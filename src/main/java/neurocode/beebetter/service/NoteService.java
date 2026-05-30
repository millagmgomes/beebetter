package neurocode.beebetter.service;

import neurocode.beebetter.dto.NoteRequestDTO;
import neurocode.beebetter.dto.NoteResponseDTO;
import neurocode.beebetter.model.Note;
import neurocode.beebetter.model.User;
import neurocode.beebetter.repository.NoteRepository;
import neurocode.beebetter.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteService {

    @Autowired private NoteRepository noteRepository;
    @Autowired private UserRepository userRepository;

    public List<NoteResponseDTO> listByUser(Long userId) {
        return noteRepository.findByUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(this::toDTO)
                .toList();
    }

    public NoteResponseDTO create(NoteRequestDTO dto) {
        User user = userRepository.findById(dto.userId())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        Note note = Note.builder()
                .title(dto.title())
                .description(dto.description())
                .user(user)
                .build();

        noteRepository.save(note);
        return toDTO(note);
    }

    public NoteResponseDTO update(Long noteId, NoteRequestDTO dto) {
        Note note = noteRepository.findById(noteId)
                .orElseThrow(() -> new RuntimeException("Anotação não encontrada"));

        note.setTitle(dto.title());
        note.setDescription(dto.description());
        noteRepository.save(note);

        return toDTO(note);
    }

    public void delete(Long noteId) {
        noteRepository.deleteById(noteId);
    }

    private NoteResponseDTO toDTO(Note note) {
        return new NoteResponseDTO(
                note.getId(),
                note.getTitle(),
                note.getDescription(),
                note.getCreatedAt()
        );
    }
}