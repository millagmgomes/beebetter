package neurocode.beebetter.repository;

import neurocode.beebetter.model.PomodoroSession;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MascotRepository extends JpaRepository<PomodoroSession, Long> {
}
