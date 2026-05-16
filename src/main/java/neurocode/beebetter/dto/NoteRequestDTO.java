package neurocode.beebetter.dto;


public record NoteRequestDTO(
        String title,
        String description,
        Long userId
) {}
