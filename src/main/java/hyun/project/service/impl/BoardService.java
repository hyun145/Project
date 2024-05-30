package hyun.project.service.impl;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import hyun.project.dto.BoardDTO;
import hyun.project.repository.BoardRepository;
import hyun.project.repository.entity.BoardEntity;
import hyun.project.service.IBoardService;
import hyun.project.util.CmmUtil;
import hyun.project.util.DateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class BoardService implements IBoardService {

    // RequiredArgsConstructor 어노테이션으로 생성자를 자동 생성함
    // noticeRepository 변수에 이미 메모리에 올라간 NoticeRepository 객체를 넣어줌
    // 예전에는 autowired 어노테이션를 통해 설정했었지만, 이젠 생성자를 통해 객체 주입함
    private final BoardRepository boardRepository;

    @Override
    public List<BoardDTO> getBoardList() {

        log.info(this.getClass().getName() + ".getBoardList Start!");

        // 공지사항 전체 리스트 조회하기
        List<BoardEntity> rList = boardRepository.findAllByOrderByBoardSeqDesc();

        // 엔티티의 값들을 DTO에 맞게 넣어주기
        List<BoardDTO> nList = new ObjectMapper().convertValue(rList,
                new TypeReference<>() {
                });



        log.info(this.getClass().getName() + ".getBoardList End!");

        return nList;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public BoardDTO getBoardInfo(BoardDTO pDTO, boolean type) {

        log.info(this.getClass().getName() + ".getBoardInfo Start!");

        if (type) {     // 상세 화면만 조회수 증가하고, 수정 화면은 조회수 증가 안함.
            // 조회수 증가하기
            int res = boardRepository.updateReadCnt(pDTO.boardSeq());

            // 조회수 증가 성공여부 체크
            log.info("res : " + res);
        }

        // 공지사항 상세내역 가져오기
        BoardEntity rEntity = boardRepository.findByBoardSeq(pDTO.boardSeq());

        // 엔티티의 값들을 DTO에 맞게 넣어주기
        BoardDTO rDTO = new ObjectMapper().convertValue(rEntity, BoardDTO.class);  // DTO 변환하기.

        log.info(this.getClass().getName() + ".getBoardInfo End!");

        return rDTO;
    }

    @Transactional
    @Override
    public int updateBoardInfo(BoardDTO pDTO) {

        log.info(this.getClass().getName() + ".updateBoardInfo Start!");

        Long boardSeq = pDTO.boardSeq();

        String title = CmmUtil.nvl(pDTO.title());
        String contents = CmmUtil.nvl(pDTO.contents());
        String userId = CmmUtil.nvl(pDTO.userId());
        String nickName = CmmUtil.nvl(pDTO.nickName());

        log.info("boardSeq : " + boardSeq);
        log.info("title : " + title);
        log.info("contents : " + contents);
        log.info("userId : " + userId);
        log.info("nickName : " + nickName);

        // 현재 공지사항 조회수 가져오기
        BoardEntity rEntity = boardRepository.findByBoardSeq(boardSeq);

        // 수정할 값들을 빌더를 통해 엔티티에 저장하기
        BoardEntity pEntity = BoardEntity.builder()
                .boardSeq(boardSeq).title(title).contents(contents).userId(userId)
                .nickName(nickName)
                .readCnt(rEntity.getReadCnt())
                .build();

        // 데이터 수정하기
        boardRepository.save(pEntity);

        log.info(this.getClass().getName() + ".updateBoardInfo End!");

        return 1;

    }

    @Override
    public void deleteBoardInfo(BoardDTO pDTO)  {

        log.info(this.getClass().getName() + ".deleteBoardInfo Start!");

        Long boardSeq = pDTO.boardSeq();

        log.info("boardSeq : " + boardSeq);

        // 데이터 수정하기
        boardRepository.deleteById(boardSeq);


        log.info(this.getClass().getName() + ".deleteBoardInfo End!");
    }

    @Override
    public Long insertBoardInfo(BoardDTO pDTO) throws Exception {

        log.info(this.getClass().getName() + ".InsertBoardInfo Start!");

        String title = CmmUtil.nvl(pDTO.title());
        String contents = CmmUtil.nvl(pDTO.contents());
        String nickName = CmmUtil.nvl(pDTO.nickName());
        String userId = CmmUtil.nvl(pDTO.userId());
        log.info("title : " + title);
        log.info("contents : " + contents);
        log.info("nickName : " + nickName);
        log.info("userId : " + userId);

        // 공지사항 저장을 위해서는 PK 값은 빌더에 추가하지 않는다.
        // JPA에 자동 증가 설정을 해놨음
        BoardEntity pEntity = BoardEntity.builder()
                .title(title).contents(contents).nickName(nickName).readCnt(0L)
                .regId(nickName).regDt(DateUtil.getDateTime("yyyy-MM-dd hh:mm:ss"))
                .chgId(nickName).chgDt(DateUtil.getDateTime("yyyy-MM-dd hh:mm:ss"))
                .userId(userId).build();

        // 공지사항 저장하기
        boardRepository.save(pEntity);

        log.info(this.getClass().getName() + ".InsertBoardInfo End!");
        return pEntity.getBoardSeq();

    }

    @Override
    public List<BoardDTO> boardSearchList(BoardDTO pDTO) throws Exception {

        log.info(this.getClass().getName() +".boardSearchList Start!");
        String keyWord = CmmUtil.nvl(pDTO.title());


        List<BoardEntity> rEntity = boardRepository.findByTitleContaining(keyWord);


        // 엔티티의 값들을 DTO에 맞게 넣어주기
        List<BoardDTO> nList = new ObjectMapper().convertValue(rEntity,
                new TypeReference<>() {
                });

        log.info(this.getClass().getName() +".boardSearchList End!");
        return nList;
    }


}