package neurocode.beebetter.repository;

import neurocode.beebetter.model.UserItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserItemRepository extends JpaRepository<UserItem, Long> {
    List<UserItem> findByUserId(Long userId);
    boolean existsByUserIdAndShopItemId(Long userId, Long shopItemId);
    Optional<UserItem> findByUserIdAndShopItemId(Long userId, Long shopItemId);
    List<UserItem> findByUserIdAndEquipped(Long userId, boolean equipped);
}