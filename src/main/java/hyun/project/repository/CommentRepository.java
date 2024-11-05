package hyun.project.repository;

import hyun.project.repository.entity.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, Long> {

    List<CommentEntity> findByBoardSeqOrderByCommentSeq(Long boardSeq) throws Exception;

    CommentEntity findByRegDt(String regDt) throws Exception;

    CommentEntity findTopByBoardSeq(Long boardSeq) throws Exception;

    Long countByBoardSeq(Long boardSeq) throws Exception;

    CommentEntity findTopByBoardSeqOrderByCommentSeqDesc(Long boardSeq) throws Exception;

    List<CommentEntity> findByBoardSeqAndCommentSeq(Long boardSeq,Long commentSeq) throws Exception;


}
