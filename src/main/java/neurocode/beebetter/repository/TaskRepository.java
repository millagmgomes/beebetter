package neurocode.beebetter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import neurocode.beebetter.model.Task;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByUserId(Long userId);

    List<Task> findByUserIdAndDueDate(Long userId, LocalDate dueDate);

    List<Task> findByUserIdAndDueDateBetween(Long userId, LocalDate start, LocalDate end);

    List<Task> findByUserIdAndDueDateAndCompletedFalseAndIsMissionFalse(Long userId, LocalDate date);

    List<Task> findByUserIdAndCompletedFalseAndIsMissionFalse(Long userId);

    List<Task> findByUserIdAndCompletedTrue(Long userId);

    List<Task> findByUserIdAndIsMissionTrueAndCompletedFalse(Long userId);

    @Query("SELECT t FROM Task t WHERE t.user.id = :userId AND t.isMission = true AND t.dueDate <= :date AND (t.recurrenceEndDate IS NULL OR t.recurrenceEndDate >= :date) AND t.completed = false")
    List<Task> findActiveMissionsByUserIdAndDate(@Param("userId") Long userId, @Param("date") LocalDate date);

}