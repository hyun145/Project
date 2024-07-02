package hyun.project.repository.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "GRADE_CRITERIA")
@Entity
@Builder
public class GradeCriteriaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "grade_id")
    private Long gradeId;

    @NonNull
    @Column(name = "sch_name")
    private String schName;


    @NonNull
    @Column(name = "depart_name")
    private String departName;

    @NonNull
    @Column(name = "min_score")
    private float minScore;

    @NonNull
    @Column(name = "avg_score")
    private float avgScore;

}
