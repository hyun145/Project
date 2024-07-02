package hyun.project.service;

import hyun.project.dto.CommentDTO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ICommentService {



    List<CommentDTO> getCommentList(CommentDTO pDTO);

    void deleteComment(String nickName);


    @Transactional(rollbackFor = Exception.class)
    void insertComment(Long noticeSeq,
                       String userId,
                       String commnet) throws Exception;


}
