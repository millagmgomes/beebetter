package neurocode.beebetter.dto;

import java.time.LocalTime;

public record AlarmResponseDTO(
        Long id,
        LocalTime time,
        String label,
        boolean active,
        String ringtone
) {}
