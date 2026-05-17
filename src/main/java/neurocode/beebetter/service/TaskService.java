package neurocode.beebetter.service;

import neurocode.beebetter.dto.GoalSummaryDTO;
import neurocode.beebetter.dto.TaskRequestDTO;
import neurocode.beebetter.dto.TaskResponseDTO;
import neurocode.beebetter.model.Task;
import neurocode.beebetter.model.User;
import neurocode.beebetter.repository.TaskRepository;
import neurocode.beebetter.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class TaskService {

    @Autowired private CoinService coinService;
    @Autowired private TaskRepository taskRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private MascotService mascotService;
    @Autowired private DailyProgressService dailyProgressService;

    public List<TaskResponseDTO> listByUser(Long userId) {
        return taskRepository.findByUserId(userId)
                .stream()
                .map(this::toDTO)
                .toList();
    }

    public List<TaskResponseDTO> listByUserAndDate(Long userId, LocalDate date) {
        // Tarefas normais do dia
        List<Task> tasks = new ArrayList<>(taskRepository.findByUserIdAndDueDate(userId, date));

        // Missões ativas naquele período
        List<Task> activeMissions = taskRepository.findActiveMissionsByUserIdAndDate(userId, date);

        // Evita duplicatas (missão que começa exatamente naquele dia)
        activeMissions.forEach(mission -> {
            if (tasks.stream().noneMatch(t -> t.getId().equals(mission.getId()))) {
                tasks.add(mission);
            }
        });

        return tasks.stream().map(this::toDTO).toList();
    }

    public TaskResponseDTO create(TaskRequestDTO dto) {
        User user = userRepository.findById(dto.userId())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        Task task = Task.builder()
                .title(dto.title())
                .description(dto.description())
                .completed(false)
                .dueDate(dto.dueDate())
                .recurrence(dto.recurrence() != null
                        ? dto.recurrence()
                        : Task.RecurrenceType.NONE)
                .recurrenceEndDate(dto.recurrenceEndDate())
                .isMission(dto.isMission())
                .targetCount(dto.isMission() ? dto.targetCount() : null)
                .currentCount(0)
                .user(user)
                .build();

        taskRepository.save(task);
        generateRecurringTasks(task);

        return toDTO(task);
    }

    @Transactional
    public TaskResponseDTO completeTask(Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Tarefa não encontrada"));

        task.setCompleted(true);
        taskRepository.save(task);

        Long userId = task.getUser().getId();
        mascotService.addExperience(userId, 20);
        dailyProgressService.registerCompletedTask(userId);
        coinService.addTaskReward(userId);

        return toDTO(task);
    }

    @Transactional
    public void generateRecurringTasks(Task originalTask) {
        if (originalTask.getRecurrence() == Task.RecurrenceType.NONE) return;

        LocalDate start = originalTask.getDueDate().plusDays(1);
        LocalDate end = originalTask.getRecurrenceEndDate() != null
                ? originalTask.getRecurrenceEndDate()
                : start.plusMonths(3);

        LocalDate current = start;

        while (!current.isAfter(end)) {
            Task recurring = Task.builder()
                    .title(originalTask.getTitle())
                    .description(originalTask.getDescription())
                    .completed(false)
                    .user(originalTask.getUser())
                    .dueDate(current)
                    .recurrence(Task.RecurrenceType.NONE)
                    .build();

            taskRepository.save(recurring);

            current = switch (originalTask.getRecurrence()) {
                case DAILY -> current.plusDays(1);
                case WEEKLY -> current.plusWeeks(1);
                case MONTHLY -> current.plusMonths(1);
                default -> end.plusDays(1);
            };
        }
    }

    private TaskResponseDTO toDTO(Task task) {
        Double progressRate = null;
        if (task.isMission()
                && task.getTargetCount() != null
                && task.getTargetCount() > 0) {
            progressRate = Math.round(
                    (task.getCurrentCount() * 100.0 / task.getTargetCount()) * 10
            ) / 10.0;
        }

        return new TaskResponseDTO(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.isCompleted(),
                task.getDueDate(),
                task.isMission(),
                task.getTargetCount(),
                task.getCurrentCount(),
                progressRate
        );
    }

    public GoalSummaryDTO getGoalSummary(Long userId, String period) {
        LocalDate start;
        LocalDate end;

        if (period.equalsIgnoreCase("WEEK")) {
            start = LocalDate.now().with(DayOfWeek.MONDAY);
            end = LocalDate.now().with(DayOfWeek.SUNDAY);
        } else {
            start = LocalDate.now();
            end = LocalDate.now();
        }

        List<Task> tasks = taskRepository.findByUserIdAndDueDateBetween(userId, start, end);

        int total = tasks.size();
        int completed = (int) tasks.stream()
                .filter(Task::isCompleted)
                .count();
        int pending = total - completed;
        double rate = total > 0 ? (completed * 100.0 / total) : 0.0;

        return new GoalSummaryDTO(total, completed, pending, rate, period.toUpperCase());
    }

    public TaskResponseDTO updateMissionProgress(Long taskId, Integer increment) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Tarefa não encontrada"));

        if (!task.isMission()) {
            throw new RuntimeException("Essa tarefa não é uma missão");
        }

        int newCount = task.getCurrentCount() + increment;
        task.setCurrentCount(newCount);

        if (newCount >= task.getTargetCount()) {
            task.setCurrentCount(task.getTargetCount());
            task.setCompleted(true);
        }

        taskRepository.save(task);
        return toDTO(task);
    }

    public List<TaskResponseDTO> getTodayTasks(Long userId) {
        return taskRepository
                .findByUserIdAndDueDateAndCompletedFalseAndIsMissionFalse(userId, LocalDate.now())
                .stream().map(this::toDTO).toList();
    }

    public List<TaskResponseDTO> getInProgressTasks(Long userId) {
        return taskRepository
                .findByUserIdAndCompletedFalseAndIsMissionFalse(userId)
                .stream().map(this::toDTO).toList();
    }

    public List<TaskResponseDTO> getCompletedTasks(Long userId) {
        return taskRepository
                .findByUserIdAndCompletedTrue(userId)
                .stream().map(this::toDTO).toList();
    }

    public List<TaskResponseDTO> getMissions(Long userId) {
        return taskRepository
                .findByUserIdAndIsMissionTrueAndCompletedFalse(userId)
                .stream().map(this::toDTO).toList();
    }
}