package neurocode.beebetter.dto;

import java.time.LocalTime;

public record AlarmRequestDTO(
        LocalTime time,
        String label,
        String ringtone
) {}

