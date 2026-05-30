package neurocode.beebetter.dto;

public record AuthResponseDTO(
        String token,
        UserResponseDTO user
) {}