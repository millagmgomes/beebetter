package neurocode.beebetter.service;

import neurocode.beebetter.dto.TaskRequestDTO;
import neurocode.beebetter.dto.TaskResponseDTO;
import neurocode.beebetter.model.Task;
import neurocode.beebetter.model.User;
import neurocode.beebetter.repository.TaskRepository;
import neurocode.beebetter.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import neurocode.beebetter.service.DailyProgressService;
import java.util.List;

@Service
public class TaskService {


    @Autowired
    private CoinService coinService;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MascotService mascotService;

    @Autowired
    private DailyProgressService dailyProgressService;


    public List<TaskResponseDTO> listByUser(Long userId) {
        return taskRepository.findByUserId(userId)
                .stream()
                .map(t -> new TaskResponseDTO(t.getId(), t.getTitle(), t.getDescription(), t.isCompleted()))
                .toList();
    }

    public TaskResponseDTO create(TaskRequestDTO dto) {
        User user = userRepository.findById(dto.userId())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        Task task = Task.builder()
                .title(dto.title())
                .description(dto.description())
                .completed(false)
                .user(user)
                .build();

        taskRepository.save(task);
        return new TaskResponseDTO(task.getId(), task.getTitle(), task.getDescription(), task.isCompleted());
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

        return new TaskResponseDTO(task.getId(), task.getTitle(), task.getDescription(), true);
    }
}
