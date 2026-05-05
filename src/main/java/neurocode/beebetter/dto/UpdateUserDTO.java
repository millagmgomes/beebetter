package neurocode.beebetter.dto;

import java.time.LocalDate;
import java.util.List;

public record UpdateUserDTO(
    LocalDate birthDate,
    String gender,
    String state,
    String city,
    String hasTdah,
    List<String> otherConditions,
    String occupation,
    List<String> symptoms
) {}