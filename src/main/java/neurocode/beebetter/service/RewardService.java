package neurocode.beebetter.service;

import neurocode.beebetter.dto.RewardResponseDTO;
import neurocode.beebetter.model.Mascot;
import neurocode.beebetter.model.Reward;
import neurocode.beebetter.repository.MascotRepository;
import neurocode.beebetter.repository.RewardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RewardService {

    @Autowired
    private RewardRepository rewardRepository;

    @Autowired
    private MascotRepository mascotRepository;

    public void checkAndUnlockRewards(Long mascotId, Integer currentXp) {
        List<Reward> toUnlock = rewardRepository
                .findByMascotIdAndUnlockedFalseAndPointsRequiredLessThanEqual(mascotId, currentXp);

        toUnlock.forEach(reward -> reward.setUnlocked(true));
        rewardRepository.saveAll(toUnlock);
    }

    public List<RewardResponseDTO> listByMascot(Long mascotId) {
        return rewardRepository.findByMascotId(mascotId)
                .stream()
                .map(r -> new RewardResponseDTO(
                        r.getId(),
                        r.getTitle(),
                        r.getDescription(),
                        r.getPointsRequired(),
                        r.isUnlocked()
                ))
                .toList();
    }
}