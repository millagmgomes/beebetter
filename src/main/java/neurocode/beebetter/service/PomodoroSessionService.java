package neurocode.beebetter.service;

import neurocode.beebetter.dto.PomodoroSessionRequestDTO;
import neurocode.beebetter.dto.PomodoroSessionResponseDTO;
import neurocode.beebetter.model.PomodoroSession;
import neurocode.beebetter.model.User;
import neurocode.beebetter.repository.PomodoroSessionRepository;
import neurocode.beebetter.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PomodoroSessionService {

    @Autowired
    private PomodoroSessionRepository pomodoroSessionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DailyProgressService dailyProgressService;

    @Autowired
    private MascotService mascotService;

    public PomodoroSessionResponseDTO start(PomodoroSessionRequestDTO dto) {
        User user = userRepository.findById(dto.userId())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        PomodoroSession session = PomodoroSession.builder()
                .durationMinutes(dto.durationMinutes())
                .startedAt(LocalDateTime.now())
                .user(user)
                .build();

        pomodoroSessionRepository.save(session);
        return toDTO(session);
    }

    public PomodoroSessionResponseDTO finish(Long sessionId) {
        PomodoroSession session = pomodoroSessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Sessão não encontrada"));

        session.setFinishedAt(LocalDateTime.now());
        pomodoroSessionRepository.save(session);

        Long userId = session.getUser().getId();

        dailyProgressService.registerFocusMinutes(userId, session.getDurationMinutes());

        mascotService.addExperience(userId, session.getDurationMinutes());

        return toDTO(session);
    }

    public List<PomodoroSessionResponseDTO> listByUser(Long userId) {
        return pomodoroSessionRepository.findByUserId(userId)
                .stream()
                .map(this::toDTO)
                .toList();
    }

    private PomodoroSessionResponseDTO toDTO(PomodoroSession s) {
        return new PomodoroSessionResponseDTO(
                s.getId(),
                s.getDurationMinutes(),
                s.getStartedAt(),
                s.getFinishedAt()
        );
    }
}