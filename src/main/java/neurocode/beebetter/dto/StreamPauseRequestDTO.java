package neurocode.beebetter.dto;

import java.time.LocalDate;

public record StreamPauseRequestDTO(
        LocalDate pauseEndDate
) {}