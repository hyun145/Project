package hyun.project.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import hyun.project.dto.CommentDTO;
import hyun.project.repository.CommentRepository;
import hyun.project.repository.entity.CommentEntity;
import hyun.project.service.ICommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;



@Slf4j
@RequiredArgsConstructor
@Service
public class CommentService implements ICommentService {

    private final CommentRepository commentRepository;

    @Override
    public List<CommentDTO> getCommentList(CommentDTO pDTO) {

        log.info("댓글 조회 서비스 시작");

        log.info("pDTO.boardSeq : " + pDTO.boardSeq());
        List<CommentEntity> rList = commentRepository.findAllByBoardSeq(pDTO.boardSeq());

        List<CommentDTO> nList = new ObjectMapper().convertValue(rList,
                new TypeReference<>() {
                });






        log.info("댓글 조회 서비스 종료");

        return nList;
    }

    @Override
    @Transactional
    public void deleteComment(String nickName) {
        log.info("댓글 삭제 서비스 시작");


        commentRepository.deleteByNickName(nickName);


        log.info("댓글 삭제 서비스 종료");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertComment(Long boardSeq, String nickName, String comment) throws Exception {

        log.info("댓글 작성 서비스 시작");
        log.info("boardSeq :" +boardSeq);
        log.info("nickName :" + nickName);
        log.info("comment :" + comment);

        LocalDateTime regDt = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);


        commentRepository.save(
                CommentEntity.builder()
                        .boardSeq(boardSeq)
                        .comment(comment)
                        .nickName(nickName)
                        .regDt(regDt)
                        .build()
        );

        log.info("댓글 작성 서비스 종료");
    }
}
