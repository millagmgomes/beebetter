package neurocode.beebetter.service;

import neurocode.beebetter.dto.ShopItemResponseDTO;
import neurocode.beebetter.dto.UserItemResponseDTO;
import neurocode.beebetter.model.DailyShop;
import neurocode.beebetter.model.ShopItem;
import neurocode.beebetter.model.User;
import neurocode.beebetter.model.UserItem;
import neurocode.beebetter.repository.DailyShopRepository;
import neurocode.beebetter.repository.ShopItemRepository;
import neurocode.beebetter.repository.UserItemRepository;
import neurocode.beebetter.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ShopService {

    @Autowired private ShopItemRepository shopItemRepository;
    @Autowired private UserItemRepository userItemRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private DailyShopRepository dailyShopRepository;

    private static final int DAILY_SHOP_SIZE = 8;

    // ── Catálogo completo ──
    public List<ShopItemResponseDTO> listAll() {
        return shopItemRepository.findAll()
                .stream()
                .map(this::toShopDTO)
                .toList();
    }

    // ── Catálogo por categoria ──
    public List<ShopItemResponseDTO> listByCategory(String category) {
        ShopItem.ItemCategory cat = ShopItem.ItemCategory.valueOf(category.toUpperCase());
        return shopItemRepository.findByCategory(cat)
                .stream()
                .map(this::toShopDTO)
                .toList();
    }

    // ── Itens do usuário (vestiário) ──
    @Transactional(readOnly = true)
    public List<UserItemResponseDTO> listUserItems(Long userId) {
        return userItemRepository.findByUserId(userId)
                .stream()
                .map(this::toUserItemDTO)
                .toList();
    }

    // ── Loja diária — 6 itens aleatórios que o usuário ainda não tem ──
    @Transactional
    public List<ShopItemResponseDTO> getDailyShop(Long userId) {
        LocalDate today = LocalDate.now();

        // IDs dos itens que o usuário já comprou (move para cima)
        List<Long> purchasedIds = userItemRepository
                .findByUserId(userId)
                .stream()
                .map(ui -> ui.getShopItem().getId())
                .toList();

        List<DailyShop> existing = dailyShopRepository
                .findByUserIdAndDate(userId, today);

        if (existing.size() == DAILY_SHOP_SIZE) {
            return existing.stream()
                    .map(ds -> toShopDTO(ds.getShopItem()))
                    .filter(dto -> !purchasedIds.contains(dto.id())) // ← filtro
                    .toList();
        }

        // Itens disponíveis (não comprados)
        List<ShopItem> available = shopItemRepository.findAll()
                .stream()
                .filter(item -> !purchasedIds.contains(item.getId()))
                .collect(Collectors.toList());

        // Se não tem itens suficientes, retorna o que tiver
        Collections.shuffle(available);
        List<ShopItem> daily = available.stream()
                .limit(DAILY_SHOP_SIZE)
                .toList();

        // Limpa sorteio antigo (caso tenha menos de 6) e salva novo
        dailyShopRepository.deleteByUserIdAndDate(userId, today);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        daily.forEach(item ->
                dailyShopRepository.save(DailyShop.builder()
                        .user(user)
                        .shopItem(item)
                        .date(today)
                        .build())
        );

        return daily.stream()
                .map(this::toShopDTO)
                .toList();
    }

    // ── Comprar item ──
    @Transactional
    public UserItemResponseDTO purchase(Long userId, Long shopItemId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        ShopItem item = shopItemRepository.findById(shopItemId)
                .orElseThrow(() -> new RuntimeException("Item não encontrado"));

        if (userItemRepository.existsByUserIdAndShopItemId(userId, shopItemId)) {
            throw new RuntimeException("Item já comprado");
        }

        if (user.getCoins() < item.getPrice()) {
            throw new RuntimeException("Moedas insuficientes");
        }

        user.setCoins(user.getCoins() - item.getPrice());
        userRepository.save(user);

        UserItem userItem = UserItem.builder()
                .user(user)
                .shopItem(item)
                .equipped(false)
                .build();

        userItemRepository.save(userItem);
        return toUserItemDTO(userItem);
    }

    // ── Equipar / desequipar item ──
    @Transactional
    public UserItemResponseDTO equip(Long userId, Long userItemId) {
        UserItem itemToEquip = userItemRepository.findById(userItemId)
                .orElseThrow(() -> new RuntimeException("Item não encontrado"));

        if (!itemToEquip.getUser().getId().equals(userId)) {
            throw new RuntimeException("Item não pertence ao usuário");
        }

        // Desequipa todos da mesma categoria
        String category = itemToEquip.getShopItem().getCategory().name();
        userItemRepository.findByUserId(userId).forEach(ui -> {
            if (ui.getShopItem().getCategory().name().equals(category)) {
                ui.setEquipped(false);
                userItemRepository.save(ui);
            }
        });

        // Toggle
        itemToEquip.setEquipped(!itemToEquip.isEquipped());
        userItemRepository.save(itemToEquip);

        return toUserItemDTO(itemToEquip);
    }

    // ── DTOs ──
    private ShopItemResponseDTO toShopDTO(ShopItem item) {
        return new ShopItemResponseDTO(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getPrice(),
                item.getAssetName(),
                item.getCategory().name()
        );
    }

    private UserItemResponseDTO toUserItemDTO(UserItem ui) {
        return new UserItemResponseDTO(
                ui.getId(),
                ui.getShopItem().getId(),
                ui.getShopItem().getName(),
                ui.getShopItem().getDescription(),
                ui.getShopItem().getAssetName(),
                ui.getShopItem().getCategory().name(),
                ui.getPurchasedAt(),
                ui.isEquipped()
        );
    }
}