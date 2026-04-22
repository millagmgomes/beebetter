package neurocode.beebetter.service;

import jakarta.transaction.Transactional;
import neurocode.beebetter.dto.DailyProgressResponseDTO;
import neurocode.beebetter.model.DailyProgress;
import neurocode.beebetter.model.User;
import neurocode.beebetter.repository.DailyProgressRepository;
import neurocode.beebetter.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class DailyProgressService {

    @Autowired
    private CoinService coinService;

    @Autowired
    private DailyProgressRepository dailyProgressRepository;

    @Autowired
    private UserRepository userRepository;


    @Transactional
    public boolean registerDailyLogin(Long userId) {
        boolean isFirstLoginToday = dailyProgressRepository
                .findByUserIdAndDate(userId, LocalDate.now())
                .isEmpty();

        if (isFirstLoginToday) {
            getOrCreateToday(userId);
            coinService.addDailyLoginReward(userId);
            return true;
        }

        return false;
    }


    @Transactional
    public void registerCompletedTask(Long userId) {
        DailyProgress progress = getOrCreateToday(userId);
        progress.setCompletedTasks(progress.getCompletedTasks() + 1);
        dailyProgressRepository.save(progress);
    }

    @Transactional
    public void registerFocusMinutes(Long userId, Integer minutes) {
        DailyProgress progress = getOrCreateToday(userId);
        progress.setFocusMinutes(progress.getFocusMinutes() + minutes);
        dailyProgressRepository.save(progress);
    }

    public DailyProgressResponseDTO getToday(Long userId) {
        DailyProgress progress = getOrCreateToday(userId);
        return toDTO(progress);
    }

    private DailyProgress getOrCreateToday(Long userId) {
        return dailyProgressRepository
                .findByUserIdAndDate(userId, LocalDate.now())
                .orElseGet(() -> {
                    User user = userRepository.findById(userId)
                            .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

                    return dailyProgressRepository.save(
                            DailyProgress.builder()
                                    .date(LocalDate.now())
                                    .completedTasks(0)
                                    .focusMinutes(0)
                                    .user(user)
                                    .build()
                    );
                });
    }

    private DailyProgressResponseDTO toDTO(DailyProgress dp) {
        return new DailyProgressResponseDTO(
                dp.getId(),
                dp.getDate(),
                dp.getCompletedTasks(),
                dp.getFocusMinutes()
        );
    }
}