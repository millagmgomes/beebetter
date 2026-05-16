package neurocode.beebetter.dto;

import java.time.LocalDateTime;

public record NoteResponseDTO(
        Long id,
        String title,
        String description,
        LocalDateTime createdAt
) {}