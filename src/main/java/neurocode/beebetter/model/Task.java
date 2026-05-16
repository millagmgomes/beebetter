package neurocode.beebetter.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    private String description;

    private boolean completed;

    private LocalDate dueDate;

    private LocalDate recurrenceEndDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private RecurrenceType recurrence = RecurrenceType.NONE;

    @Builder.Default
    private boolean isMission = false;

    @Builder.Default
    private Integer targetCount = 0;

    @Builder.Default
    private Integer currentCount = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public enum RecurrenceType {
        NONE, DAILY, WEEKLY, MONTHLY
    }
}