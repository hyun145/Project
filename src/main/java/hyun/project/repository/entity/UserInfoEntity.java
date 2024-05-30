package hyun.project.repository.entity;



import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.io.Serializable;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "USER_INFO")
@DynamicInsert
@DynamicUpdate
@Builder
@Entity
public class UserInfoEntity implements Serializable {

    @Id
    @Column(name = "USER_ID")
    private String userId;

    @NonNull
    @Column(name = "USER_NAME", length = 500, nullable = false)
    private String userName;

    @NonNull
    @Column(name = "PASSWORD", length = 500, nullable = false)
    private String password;

    @NonNull
    @Column(name = "EMAIL", length = 100,nullable = false)
    private String email;


    @NonNull
    @Column(name = "NICKNAME", length = 500, nullable = false)
    private String nickName;

    @Column(name = "reg_id", updatable = false)
    private String regId;

    @Column(name = "reg_dt", updatable = false)
    private String regDt;


    @Column(name = "chg_id")
    private String chgId;


    @Column(name = "chg_dt")
    private String chgDt;


}














