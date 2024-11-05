package hyun.project.service;

import hyun.project.dto.CommentDTO;

import java.util.List;

public interface ICommentService {



    List<CommentDTO> getCommentList(final Long boardSeq) throws Exception;



    void insertComment(final Long noticeSeq,
                       final String userId,
                       final String contents,
                       final int dept,
                       final String dt,
                       final String nickname) throws Exception;


    void updateComment(final Long boarSeq,
                       final Long commentSeq,
                       final String userId,
                       final String contents,
                       final String dt) throws Exception;

    void deleteComment(final Long boardSeq,
                       final Long commentSeq,
                       final String userId) throws Exception;


}
