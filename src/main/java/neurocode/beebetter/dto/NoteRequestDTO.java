package neurocode.beebetter.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record NoteRequestDTO(
        String title,
        String description,
        @JsonProperty("userId") Long userId
) {}
