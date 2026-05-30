package neurocode.beebetter.controller;

import neurocode.beebetter.dto.ShopItemResponseDTO;
import neurocode.beebetter.dto.UserItemResponseDTO;
import neurocode.beebetter.service.ShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/shop")
public class ShopController {

    @Autowired private ShopService shopService;

    // GET /shop/items — catálogo completo
    @GetMapping("/items")
    public ResponseEntity<List<ShopItemResponseDTO>> listAll() {
        return ResponseEntity.ok(shopService.listAll());
    }

    // GET /shop/items?category=HAT — por categoria
    @GetMapping("/items/category/{category}")
    public ResponseEntity<List<ShopItemResponseDTO>> listByCategory(
            @PathVariable String category) {
        return ResponseEntity.ok(shopService.listByCategory(category));
    }

    // GET /shop/user/{userId}/items — itens do usuário (vestiário)
    @GetMapping("/user/{userId}/items")
    public ResponseEntity<List<UserItemResponseDTO>> listUserItems(
            @PathVariable Long userId) {
        return ResponseEntity.ok(shopService.listUserItems(userId));
    }

    // POST /shop/user/{userId}/purchase/{shopItemId} — comprar item
    @PostMapping("/user/{userId}/purchase/{shopItemId}")
    public ResponseEntity<UserItemResponseDTO> purchase(
            @PathVariable Long userId,
            @PathVariable Long shopItemId) {
        return ResponseEntity.ok(shopService.purchase(userId, shopItemId));
    }

    // PATCH /shop/user/{userId}/equip/{userItemId} — equipar/desequipar
    @PatchMapping("/user/{userId}/equip/{userItemId}")
    public ResponseEntity<UserItemResponseDTO> equip(
            @PathVariable Long userId,
            @PathVariable Long userItemId) {
        return ResponseEntity.ok(shopService.equip(userId, userItemId));
    }

    @GetMapping("/user/{userId}/daily")
    public ResponseEntity<List<ShopItemResponseDTO>> getDailyShop(
            @PathVariable Long userId) {
        return ResponseEntity.ok(shopService.getDailyShop(userId));
    }
}