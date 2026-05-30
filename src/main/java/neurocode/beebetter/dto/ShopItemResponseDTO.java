package neurocode.beebetter.dto;

// ── Catálogo ──
public record ShopItemResponseDTO(
        Long id,
        String name,
        String description,
        Integer price,
        String assetName,
        String category
) {}

// ── Item comprado pelo usuário ──
// (usado como resposta de compra e listagem do vestiário)