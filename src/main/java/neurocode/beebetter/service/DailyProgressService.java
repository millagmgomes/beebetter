package neurocode.beebetter.service;

import jakarta.transaction.Transactional;
import neurocode.beebetter.dto.DailyProgressResponseDTO;
import neurocode.beebetter.dto.SleepRequestDTO;
import neurocode.beebetter.model.DailyProgress;
import neurocode.beebetter.model.User;
import neurocode.beebetter.repository.DailyProgressRepository;
import neurocode.beebetter.repository.TaskRepository;
import neurocode.beebetter.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class DailyProgressService {

    @Autowired private CoinService coinService;
    @Autowired private DailyProgressRepository dailyProgressRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private TaskRepository taskRepository;
    @Autowired private StreakService streakService;

    @Transactional
    public boolean registerDailyLogin(Long userId) {
        boolean isFirstLoginToday = dailyProgressRepository
                .findByUserIdAndDate(userId, LocalDate.now())
                .isEmpty();

        if (isFirstLoginToday) {
            getOrCreateToday(userId);

            if (!streakService.isOnPause(userId)) {
                coinService.addDailyLoginReward(userId);
            }
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
        return toDTO(getOrCreateToday(userId));
    }

    public List<DailyProgressResponseDTO> getHistory(Long userId, LocalDate start, LocalDate end) {
        return dailyProgressRepository
                .findByUserIdAndDateBetween(userId, start, end)
                .stream()
                .map(this::toDTO)
                .toList();
    }

    public DailyProgressResponseDTO saveMood(Long userId, DailyProgress.Mood mood) {
        DailyProgress progress = getOrCreateToday(userId);
        progress.setMood(mood);
        dailyProgressRepository.save(progress);
        return toDTO(progress);
    }

    public DailyProgressResponseDTO saveSleep(Long userId, SleepRequestDTO dto) {
        DailyProgress progress = getOrCreateToday(userId);
        progress.setSleepTime(dto.sleepTime());
        progress.setWakeTime(dto.wakeTime());
        dailyProgressRepository.save(progress);
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

    private Double calculateSleepDuration(LocalTime sleepTime, LocalTime wakeTime) {
        if (sleepTime == null || wakeTime == null) return null;
        long minutes = Duration.between(sleepTime, wakeTime).toMinutes();
        if (minutes < 0) minutes += 24 * 60;
        return Math.round(minutes / 60.0 * 10) / 10.0;
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
                totalTasks,
                dp.getMood(),
                dp.getSleepTime(),
                dp.getWakeTime(),
                calculateSleepDuration(dp.getSleepTime(), dp.getWakeTime())
        );
    }
}