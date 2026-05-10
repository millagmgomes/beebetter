package neurocode.beebetter.repository;

import neurocode.beebetter.model.DailyProgress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.List;

public interface DailyProgressRepository extends JpaRepository<DailyProgress, Long> {

    Optional<DailyProgress> findByUserIdAndDate(Long userId, LocalDate date);

    List<DailyProgress> findByUserIdAndDateBetween(Long userId, LocalDate start, LocalDate end);

}