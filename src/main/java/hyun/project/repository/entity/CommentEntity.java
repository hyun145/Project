package hyun.project.repository.entity;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDateTime;


@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "COMMENT")
@DynamicUpdate
@DynamicInsert
@Entity
@Builder
public class CommentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "COMMENT_SEQ")
    private int commentSeq;        // 댓글 순번

    @NonNull
    @Column(name= "BOARD_SEQ")
    private Long boardSeq;      // 게시물 순번

    @NonNull
    @Column(name = "NICKNAME")
    private String nickName;        // 닉네임


    @NonNull
    @Column(name = "COMMENT")
    private String comment;        // 댓글 내용

    @NonNull
    @Column(name = "REG_DT")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class) //직렬화 역직렬화
    private LocalDateTime regDt;        // 작성 시간






}
