package hyun.project.repository;

import hyun.project.repository.entity.ScoreEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ScoreRepository extends JpaRepository<ScoreEntity, Long> {


    Optional<ScoreEntity> deleteByUserId(String userId);

    List<ScoreEntity> findByUserId(String userId);



}
