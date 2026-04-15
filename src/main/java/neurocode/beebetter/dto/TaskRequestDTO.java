package neurocode.beebetter.dto;

public record TaskRequestDTO(
        String title,
        String description,
        Long userId
) {}