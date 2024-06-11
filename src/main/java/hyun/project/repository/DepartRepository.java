package hyun.project.repository;

import hyun.project.dto.DepartDTO;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartRepository extends MongoRepository<DepartDTO, String> {





}
