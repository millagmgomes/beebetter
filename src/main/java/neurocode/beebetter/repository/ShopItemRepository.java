package neurocode.beebetter.repository;

import neurocode.beebetter.model.ShopItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ShopItemRepository extends JpaRepository<ShopItem, Long> {
    List<ShopItem> findByCategory(ShopItem.ItemCategory category);
}