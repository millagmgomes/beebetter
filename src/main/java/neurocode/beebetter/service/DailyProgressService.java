package neurocode.beebetter.service;

import jakarta.transaction.Transactional;
import neurocode.beebetter.dto.DailyProgressResponseDTO;
import neurocode.beebetter.model.DailyProgress;
import neurocode.beebetter.model.User;
import neurocode.beebetter.repository.DailyProgressRepository;
import neurocode.beebetter.repository.TaskRepository;
import neurocode.beebetter.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class DailyProgressService {

    @Autowired
    private CoinService coinService;

    @Autowired
    private DailyProgressRepository dailyProgressRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskRepository taskRepository;


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

    public List<DailyProgressResponseDTO> getHistory(Long userId, LocalDate start, LocalDate end) {
        return dailyProgressRepository.findByUserIdAndDateBetween(userId, start, end)
                .stream()
                .map(this::toDTO)
                .toList();
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
        int totalTasks = taskRepository
                .findByUserIdAndDueDate(dp.getUser().getId(), dp.getDate())
                .size();

        return new DailyProgressResponseDTO(
                dp.getId(),
                dp.getDate(),
                dp.getCompletedTasks(),
                dp.getFocusMinutes(),
                totalTasks
        );
    }
}