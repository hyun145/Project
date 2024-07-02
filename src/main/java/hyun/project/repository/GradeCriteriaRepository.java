package hyun.project.repository;

import hyun.project.repository.entity.GradeCriteriaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GradeCriteriaRepository extends JpaRepository<GradeCriteriaEntity, Long> {

    Optional<GradeCriteriaEntity> findBySchNameAndDepartName(String university, String departName);

}
