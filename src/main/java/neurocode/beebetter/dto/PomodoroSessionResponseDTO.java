package neurocode.beebetter.dto;

import java.time.LocalDateTime;

public record PomodoroSessionResponseDTO(
        Long id,
        Integer durationMinutes,
        LocalDateTime startedAt,
        LocalDateTime finishedAt
) {}
