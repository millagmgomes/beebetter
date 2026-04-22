package neurocode.beebetter.dto;

import java.time.LocalDate;

public record UserResponseDTO(
        Long id,
        String name,
        String email,
        LocalDate birthDate,
        Integer coins,
        String profilePictureUrl
) {}