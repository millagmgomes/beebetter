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

        // Loop para suportar múltiplos level ups de uma vez
        while (totalXp >= mascot.getLevel() * 100) {
            totalXp -= mascot.getLevel() * 100;
            mascot.setLevel(mascot.getLevel() + 1);
        }

        mascot.setExperience(totalXp);
        mascotRepository.save(mascot);

        rewardService.checkAndUnlockRewards(mascot.getId(), mascot.getExperience());
    }
}