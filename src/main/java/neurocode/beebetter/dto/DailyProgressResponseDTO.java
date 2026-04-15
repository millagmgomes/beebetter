package neurocode.beebetter.dto;

import java.time.LocalDate;

public record DailyProgressResponseDTO(
        Long id,
        LocalDate date,
        Integer completedTasks,
        Integer focusMinutes
) {}