package neurocode.beebetter.dto;

import java.time.LocalDateTime;

public record UserItemResponseDTO(
        Long id,
        Long shopItemId,
        String name,
        String description,
        String assetName,
        String category,
        LocalDateTime purchasedAt,
        boolean equipped
) {}