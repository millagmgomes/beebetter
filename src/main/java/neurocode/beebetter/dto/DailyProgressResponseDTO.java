package neurocode.beebetter.dto;

import neurocode.beebetter.model.DailyProgress;

import java.time.LocalDate;
import java.time.LocalTime;

public record DailyProgressResponseDTO(
        Long id,
        LocalDate date,
        Integer completedTasks,
        Integer focusMinutes,
        Integer totalTasks,
        DailyProgress.Mood mood,
        LocalTime sleepTime,
        LocalTime wakeTime,
        Double sleepDuration
) {}