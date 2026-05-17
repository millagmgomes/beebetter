package neurocode.beebetter.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record PomodoroSessionRequestDTO(
        @JsonProperty("userId") Long userId,
        @JsonProperty("durationMinutes") Integer durationMinutes
) {}