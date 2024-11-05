package hyun.project.repository.entity;

import hyun.project.repository.entity.PK.CommentPK;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;


@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "COMMENT")
@DynamicUpdate
@DynamicInsert
@Entity
@Builder
@IdClass(CommentPK.class)
public class CommentEntity {

    @Id
    @Column(name = "COMMENT_SEQ")
    private Long commentSeq;        // 댓글 순번


    @Id
    @Column(name= "BOARD_SEQ")
    private Long boardSeq;

    @Column(name = "NICKNAME")
    private String nickname;


    @Column(name = "CONTENTS")
    private String contents;


    @Column(name = "REG_ID")
    private String regId;


    @Column(name = "REG_DT", updatable = false)
    private String regDt;

    @Column(name = "CHG_DT")
    private String chgDt;

    @Column(name = "dept", updatable = false)
    private int dept;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "REG_ID", insertable = false, updatable = false)
    private UserInfoEntity userInfo;

}
