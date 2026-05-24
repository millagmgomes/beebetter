package neurocode.beebetter.repository;

import neurocode.beebetter.model.DailyShop;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface DailyShopRepository extends JpaRepository<DailyShop, Long> {
    List<DailyShop> findByUserIdAndDate(Long userId, LocalDate date);
    void deleteByUserIdAndDate(Long userId, LocalDate date);
}
