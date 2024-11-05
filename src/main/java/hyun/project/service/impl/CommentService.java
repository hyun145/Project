package hyun.project.service.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import hyun.project.dto.CommentDTO;
import hyun.project.repository.CommentRepository;
import hyun.project.repository.entity.CommentEntity;
import hyun.project.repository.entity.QCommentEntity;
import hyun.project.repository.entity.QUserInfoEntity;
import hyun.project.service.ICommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Slf4j
@RequiredArgsConstructor
@Service
public class CommentService implements ICommentService {


    private final JPAQueryFactory queryFactory;

    private final CommentRepository commentRepository;

    @Override
    public List<CommentDTO> getCommentList(Long boardSeq)  throws Exception {

        log.info("댓글 리스트 가져오기 서비스 시작");

        QUserInfoEntity ue = QUserInfoEntity.userInfoEntity;
        QCommentEntity ce = QCommentEntity.commentEntity;

        List<CommentEntity> cList = queryFactory
                .selectFrom(ce)
                .join(ce.userInfo, ue).fetchJoin()
                .where(ce.boardSeq.eq(boardSeq))
                .fetch();


        List<CommentDTO> dtoList = new ArrayList<>();


        cList.forEach(commentEntity ->
                dtoList.add(CommentDTO.from(commentEntity)));



        log.info("댓글 리스트 가져오기 서비스 종료");



        return dtoList;

    }


    /**
     * 댓글 등록
     */
    @Override
    public void insertComment(Long boardSeq,
                              String userId,
                              String contents,
                              int dept,
                              String dt,
                              final String nickname) throws Exception {

        log.info("댓글 작성 서비스 시작");

        Long commentSeq = Optional.ofNullable(
                commentRepository.findTopByBoardSeqOrderByCommentSeqDesc(boardSeq))
                .map(CommentEntity:: getCommentSeq)
                .orElse(0L);

        CommentEntity entity = CommentEntity.builder()
                .boardSeq(boardSeq)
                .commentSeq(commentSeq + 1)
                .regId(userId)
                .contents(contents)
                .regDt(dt)
                .chgDt(dt)
                .nickname(nickname)
                .build();
        commentRepository.save(entity);



        log.info("댓글 작성 서비스 종료");
        
        
//        ---- 여기서부터 다시
    }

    /**
     * 댓글 수정하기.
     */
    @Override
    @Transactional
    public void updateComment(Long boardSeq, Long commentSeq,String userId, String contents, String dt) throws Exception {

        log.info("댓글 수정 서비스 시작");

        CommentEntity entity = CommentEntity.builder()
                .boardSeq(boardSeq)
                .commentSeq(commentSeq)
                .regId(userId)
                .contents(contents)
                .chgDt(dt)
                .build();
        commentRepository.save(entity);
        log.info("댓글 수정 서비스 종료");

    }

    @Override
    @Transactional
    public void deleteComment(Long boardSeq, Long commentSeq, String userId) throws Exception {

        log.info("댓글 삭제 서비스 시작");

        commentRepository.delete(CommentEntity.builder()
                .boardSeq(boardSeq)
                .commentSeq(commentSeq)
                .regId(userId).build());

        commentRepository.deleteAll(commentRepository.findByBoardSeqAndCommentSeq(boardSeq, commentSeq));


        log.info("댓글 삭제 서비스 종료");
    }


}
