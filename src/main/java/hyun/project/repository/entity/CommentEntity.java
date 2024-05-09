package hyun.project.repository.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "COMMENT")
@DynamicInsert
@DynamicUpdate
@Builder
@Entity
public class CommentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_seq")
    private Long comemntSeq;

    @NonNull
    @JoinColumn(name = "notice_seq")
    private Long boardSeq;

    @NonNull
    @Column(name = "contents")
    private String contents;

    @Column(name = "reg_dt")
    private String regDt;

    @Column(name = "chg_dt")
    private String chgDt;

}
