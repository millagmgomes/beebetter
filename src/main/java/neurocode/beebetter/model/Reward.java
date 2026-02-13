package neurocode.beebetter.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reward {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String description;

    private Integer pointsRequired;

    private boolean unlocked;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mascot_id")
    private Mascot mascot;
}
