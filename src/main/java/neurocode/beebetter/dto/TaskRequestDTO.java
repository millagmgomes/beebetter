package neurocode.beebetter.dto;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;

public record TaskRequestDTO(
        String title,
        String description,
        Long userId,
        LocalDate dueDate
) {}