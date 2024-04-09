package hyun.project.repository.entity;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "USER_INFO")
@DynamicUpdate
@DynamicInsert
@Builder
@Cacheable
@Entity
public class UserInfoEntity {

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

    @Column(name = "roles") //권한 데이터는 ,를 구분자로 여러 개(예 : 관리자, 일반사용자) 정의 가능함
    private String roles;

}














