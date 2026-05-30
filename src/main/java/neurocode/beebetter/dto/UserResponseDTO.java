package neurocode.beebetter.dto;

import java.time.LocalDate;
import java.util.List;

public record UserResponseDTO(
        Long id,
        String name,
        String email,
        LocalDate birthDate,
        Integer coins,
        String profilePictureUrl,
        String gender,
        String state,
        String city,
        String hasTdah,
        List<String> otherConditions,
        String occupation,
        List<String> symptoms,
        Integer mascotLevel,
        Integer mascotExperience
) {}