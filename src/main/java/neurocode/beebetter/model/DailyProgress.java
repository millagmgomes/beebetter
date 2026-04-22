package neurocode.beebetter.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "daily_progress")
public class DailyProgress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate date;

    @Builder.Default
    private Integer completedTasks = 0;    // ← default explícito

    @Builder.Default
    private Integer focusMinutes = 0;      // ← default explícito

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}