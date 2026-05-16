package neurocode.beebetter.dto;

import java.time.LocalDate;

public record TaskResponseDTO(
        Long id,
        String title,
        String description,
        boolean completed,
        LocalDate dueDate,
        boolean isMission,
        Integer targetCount,
        Integer currentCount,
        Double progressRate
) {}