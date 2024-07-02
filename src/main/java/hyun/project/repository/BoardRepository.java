package hyun.project.repository;


import hyun.project.repository.entity.BoardEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardRepository extends JpaRepository<BoardEntity, Long> {

    /**
     * 공지사항 리스트
     */
    List<BoardEntity> findAllByOrderByBoardSeqDesc();




    BoardEntity findByBoardSeq(Long boardSeq);

    Page<BoardEntity> findAllByUserId(String userId, Pageable page);

    List<BoardEntity> findAllByTitleContaining(String keyWord);

    List<BoardEntity> findByTitleContaining(String keyWord);



    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE BOARD A SET A.READ_CNT = IFNULL(A.READ_CNT, 0) + 1 WHERE A.BOARD_SEQ = ?1",
            nativeQuery = true)
    int updateReadCnt(Long boardSeq);





}




































