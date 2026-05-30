package neurocode.beebetter.repository;

import neurocode.beebetter.model.Mascot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MascotRepository extends JpaRepository<Mascot, Long> {
    Optional<Mascot> findByUserId(Long userId);
}
