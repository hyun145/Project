package hyun.project.repository;


import hyun.project.repository.entity.BoardEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BoardRepository extends JpaRepository<BoardEntity, Long> {

    /**
     * 공지사항 리스트
     */
    List<BoardEntity> findAllByOrderByBoardSeqDesc();

    @Query(value =
            "SELECT " +
                    "B.BOARD_SEQ," +
                    "B.REG_ID, " +
                    "B.TITLE," +
                    "B.CONTENTS," +
                    "U.NICKNAME," +
                    "TO_CHAR(B.REG_DT, 'yyyy-MM-dd') AS REG_DT, " +
                    "B.CHG_DT," +
                    "B.READ_CNT," +
                    "(SELECT COUNT(C.COMMENT_SEQ)" +
                    "FROM COMMENT C " +
                    "WHERE C.BOARD_SEQ = B.BOARD_SEQ) AS COMMENT_CNT, " +
                    "CASE WHEN EXISTS (" +
                    "SELECT 1 FROM FILE F WHERE B.BOARD_SEQ = F.BOARD_SEQ" +
                    ") THEN 'Y' ELSE 'N' END AS FILE_YN " +
                    "FROM BOARD B " +
                    "LEFT JOIN USER_INFO U ON B.REG_ID = U.USER_ID " +
                    "ORDER BY B.NOTICE_YN DESC, B.BOARD_SEQ DESC",
            nativeQuery = true)
    Optional<List<BoardEntity>> getBoardList();



    BoardEntity findByBoardSeq(Long boardSeq);

    List<BoardEntity> findByTitleContaining(String keyWord);




    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE BOARD A SET A.READ_CNT = IFNULL(A.READ_CNT, 0) + 1 WHERE A.BOARD_SEQ = ?1",
            nativeQuery = true)
    int updateReadCnt(Long boardSeq);





}




































