package hyun.project.repository.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "FILE")
@DynamicUpdate
@DynamicInsert
@Builder
@Entity
public class FileEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "FILE_SEQ")
    private Long fileSeq;

    @Column(name = "BOARD_SEQ", nullable = false)
    private Long boardSeq;

    @Column(name = "SAVE_FILE_URL", nullable = false)
    private String saveFileUrl;

    @Column(name = "SAVE_FILE_NAME", nullable = false)
    private String saveFileName;

    @Column(name = "SAVE_FILE_PATH", nullable = false)
    private String saveFilePath;

    @Column(name = "ORG_FILE_NAME", nullable = false)
    private String orgFileName;


    @Column(name = "FILE_SIZE", nullable = false)
    private String fileSize;

}
