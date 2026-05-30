package neurocode.beebetter.dto;

public record RewardResponseDTO(
        Long id,
        String title,
        String description,
        Integer pointsRequired,
        boolean unlocked
) {}