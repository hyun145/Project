package hyun.project.repository;

import hyun.project.repository.entity.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, Long> {

    List<CommentEntity> findAllByOrderByCommentSeqDesc();

    List<CommentEntity> findAllByCommentSeq(Long noticeSeq);     // 댓글 수정


    List<CommentEntity> findAllByBoardSeq(Long noticeSeq);



}
