package neurocode.beebetter.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

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
    private Integer completedTasks = 0;

    @Builder.Default
    private Integer focusMinutes = 0;

    @Enumerated(EnumType.STRING)
    private Mood mood;

    private LocalTime sleepTime;

    private LocalTime wakeTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public enum Mood {
        POSITIVE, NEUTRAL, NEGATIVE
    }
}