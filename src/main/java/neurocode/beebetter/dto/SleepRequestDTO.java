package neurocode.beebetter.dto;

import java.time.LocalTime;

public record SleepRequestDTO(
        LocalTime sleepTime,
        LocalTime wakeTime
) {}