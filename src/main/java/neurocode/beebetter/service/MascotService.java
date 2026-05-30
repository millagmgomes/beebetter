package neurocode.beebetter.service;

import neurocode.beebetter.model.Mascot;
import neurocode.beebetter.repository.MascotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MascotService {

    @Autowired
    private MascotRepository mascotRepository;

    @Autowired
    private RewardService rewardService;

    public void addExperience(Long userId, int xp) {
        Mascot mascot = mascotRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Mascote não encontrado"));

        int totalXp = mascot.getExperience() + xp;
        mascot.setExperience(totalXp);

        if (totalXp >= mascot.getLevel() * 100) {
            mascot.setLevel(mascot.getLevel() + 1);
            mascot.setExperience(0);
        }

        mascotRepository.save(mascot);


        rewardService.checkAndUnlockRewards(mascot.getId(), mascot.getExperience());
    }
}