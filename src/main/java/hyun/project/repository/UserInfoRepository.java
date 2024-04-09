package hyun.project.repository;

import hyun.project.dto.UserInfoDTO;
import hyun.project.repository.entity.UserInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserInfoRepository extends JpaRepository<UserInfoEntity, String> {

Optional<UserInfoEntity> findByUserId(String userId);

Optional<UserInfoEntity> findByEmail(String email);

Optional<UserInfoEntity> findByNickName(String nickName);

Optional<UserInfoEntity> findByUserNameAndEmail(String userName, String Email);

Optional<UserInfoEntity> findByUserIdAndEmail(String userId, String Email);



    /*@Modifying(clearAutomatically = true)
    @Query(value = "UPDATE USER_INFO SET PASSWORD = #{PASSWORD} WHERE USER_ID = #{USER_ID}",
            nativeQuery = true)
    int updatePassword(UserInfoDTO pDTO);*/

    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE USER_INFO SET PASSWORD = :#{#pDTO.password} WHERE USER_ID = :#{#pDTO.userId}",
            nativeQuery = true)
    int updatePassword(@Param("pDTO") UserInfoDTO pDTO);



    // Optional은 NPL을 방지하기 위함임.
    Optional<UserInfoEntity> findByUserIdAndPassword(String userId, String password);




}
