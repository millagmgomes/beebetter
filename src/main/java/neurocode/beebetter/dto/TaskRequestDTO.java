package neurocode.beebetter.dto;
import com.fasterxml.jackson.annotation.JsonProperty;
import neurocode.beebetter.model.Task;

import java.time.LocalDate;

public record TaskRequestDTO(
        String title,
        String description,
        Long userId,
        LocalDate dueDate,
        Task.RecurrenceType recurrence,
        LocalDate recurrenceEndDate,
        boolean isMission,
        Integer targetCount

) {}