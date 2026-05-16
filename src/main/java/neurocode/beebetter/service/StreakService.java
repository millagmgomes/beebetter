package neurocode.beebetter.service;

import neurocode.beebetter.dto.StreamPauseRequestDTO;
import neurocode.beebetter.model.User;
import neurocode.beebetter.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
public class StreakService {

    @Autowired private UserRepository userRepository;

    @Transactional
    public String pause(Long userId, StreamPauseRequestDTO dto) {
        User user = findUser(userId);

        if (user.isStreakPaused()) {
            throw new RuntimeException("Ofensiva já está pausada");
        }

        if (dto.pauseEndDate().isBefore(LocalDate.now().plusDays(1))) {
            throw new RuntimeException("A data de retorno deve ser pelo menos amanhã");
        }

        user.setStreakPaused(true);
        user.setPauseStartDate(LocalDate.now());
        user.setPauseEndDate(dto.pauseEndDate());
        userRepository.save(user);

        return "Ofensiva pausada até " + dto.pauseEndDate();
    }

    @Transactional
    public String resume(Long userId) {
        User user = findUser(userId);

        if (!user.isStreakPaused()) {
            throw new RuntimeException("Ofensiva não está pausada");
        }

        user.setStreakPaused(false);
        user.setPauseStartDate(null);
        user.setPauseEndDate(null);
        userRepository.save(user);

        return "Ofensiva retomada!";
    }

    @Transactional
    public boolean isOnPause(Long userId) {
        User user = findUser(userId);

        if (!user.isStreakPaused()) return false;

        if (LocalDate.now().isAfter(user.getPauseEndDate())) {
            resume(userId);
            return false;
        }

        return true;
    }

    private User findUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }
}