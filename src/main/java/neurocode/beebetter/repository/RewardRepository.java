package neurocode.beebetter.repository;

import neurocode.beebetter.model.Reward;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RewardRepository extends JpaRepository<Reward, Long> {

    List<Reward> findByMascotId(Long mascotId);

    List<Reward> findByMascotIdAndUnlockedFalseAndPointsRequiredLessThanEqual(
            Long mascotId, Integer points
    );
}
