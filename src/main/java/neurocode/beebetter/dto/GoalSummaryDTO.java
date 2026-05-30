package neurocode.beebetter.dto;

public record GoalSummaryDTO(
        int totalTasks,
        int completedTasks,
        int pendingTasks,
        double completionRate,
        String period
) {}