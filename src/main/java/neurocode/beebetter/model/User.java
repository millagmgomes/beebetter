package neurocode.beebetter.model;

import jakarta.persistence.*;
import lombok.*;
import neurocode.beebetter.model.Task;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "users")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    private LocalDate birthDate;

    @Column(nullable = false)
    @Builder.Default
    private Integer coins = 0;

    private String profilePictureUrl;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Task> tasks;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PomodoroSession> pomodoroSessions;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Mascot mascot;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<DailyProgress> dailyProgresses;
}