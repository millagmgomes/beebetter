package neurocode.beebetter.dto;

import java.time.LocalDate;

public record RegisterRequestDTO(
        String name,
        String email,
        String password,
        LocalDate birthDate
) {}