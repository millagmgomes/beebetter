package neurocode.beebetter.model;

import jakarta.persistence.*;
import lombok.*;

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

    private String gender;
    private String state;
    private String city;
    private String hasTdah;
    private String occupation;

    @ElementCollection
    @CollectionTable(name = "user_other_conditions", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "condition")
    private List<String> otherConditions;

    @ElementCollection
    @CollectionTable(name = "user_symptoms", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "symptom")
    private List<String> symptoms;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Task> tasks;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PomodoroSession> pomodoroSessions;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Mascot mascot;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<DailyProgress> dailyProgresses;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Alarm> alarms;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Note> notes;

    // ← itens comprados na loja
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<UserItem> items;

    private boolean streakPaused;

    private LocalDate pauseStartDate;

    private LocalDate pauseEndDate;
}