package neurocode.beebetter.repository;

import neurocode.beebetter.model.PomodoroSession;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PomodoroSessionRepository extends JpaRepository<PomodoroSession, Long> {
}
