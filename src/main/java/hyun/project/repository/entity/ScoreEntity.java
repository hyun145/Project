package hyun.project.repository.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "SCORE")
@DynamicInsert
@DynamicUpdate
@Builder
@Entity
public class ScoreEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "score_id")
    private Long scoreId;


    @NonNull
    @Column(name = "user_id")
    private String userId;

    @NonNull
    @Column(name = "subject")
    private String subject;

    @Column(name = "raw_score")
    private int rawScore;

    @Column(name = "standard_score")
    private int standardScore;

    @Column(name = "percentile")
    private int percentile;

    @Column(name = "grade")
    private int grade;

}
