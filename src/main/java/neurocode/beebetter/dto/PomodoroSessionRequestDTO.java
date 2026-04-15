package neurocode.beebetter.dto;

public record PomodoroSessionRequestDTO(
        Long userId,
        Integer durationMinutes
) {}