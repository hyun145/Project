package hyun.project.service;

import hyun.project.dto.BoardDTO;
import hyun.project.repository.entity.BoardEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface IBoardService {


    Page<BoardDTO> getBoardList(Pageable page, String keyword) throws Exception;

    BoardDTO getBoardInfo(BoardDTO pDTO, boolean type) throws Exception; // 공지사항 상세 가져오기 위한 정보  type : 조회수 증가 여부


    int updateBoardInfo(BoardDTO pDTO) throws Exception;     // 업데이트

    void deleteBoardInfo(BoardDTO pDTO) throws Exception;     // 제거

    Long insertBoardInfo(BoardDTO pDTO) throws Exception;     // 저장


    List<BoardDTO> boardSearchList(BoardDTO pDTO) throws Exception;

    void deleteBoardByUserId(String userId) throws Exception;

    Page<BoardEntity> findByBoardByUserId(String userId, Pageable page) throws Exception;

}
