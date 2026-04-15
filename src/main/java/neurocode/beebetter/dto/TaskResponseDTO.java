package neurocode.beebetter.dto;

public record TaskResponseDTO(
        Long id,
        String title,
        String description,
        boolean completed
) {}