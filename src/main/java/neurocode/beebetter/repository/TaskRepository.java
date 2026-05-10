package neurocode.beebetter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import neurocode.beebetter.model.Task;
import java.time.LocalDate;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByUserId(Long userId);

    List<Task> findByUserIdAndDueDate(Long userId, LocalDate dueDate);
}