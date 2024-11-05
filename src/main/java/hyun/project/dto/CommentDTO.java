package hyun.project.dto;

import hyun.project.repository.entity.CommentEntity;
import lombok.Builder;

@Builder
public record CommentDTO
(

        Long commentSeq, //댓글 번호

        Long boardSeq, //게시글 번호

        String nickname, //작성자 닉네임

        String contents, //댓글 내용

        String regDt,   // 작성 시간

        String chgDt,   // 수정 시간

        String regId,    // 작성자


        int dept

)


{

        public static CommentDTO from(CommentEntity entity) {
                return CommentDTO.builder()
                        .commentSeq(entity.getCommentSeq())
                        .boardSeq(entity.getBoardSeq())
                        .regId(entity.getRegId())
                        .contents(entity.getContents())
                        .regDt(entity.getRegDt())
                        .chgDt(entity.getChgDt())
                        .dept(entity.getDept())
                        .nickname(entity.getUserInfo().getNickName()).build();
        }

        public static CommentDTO myComment(CommentEntity entity) {
                return CommentDTO.builder()
                        .commentSeq(entity.getCommentSeq())
                        .boardSeq(entity.getBoardSeq())
                        .regId(entity.getRegId())
                        .contents(entity.getContents())
                        .regDt(entity.getRegDt())
                        .dept(entity.getDept())
                        .chgDt(entity.getChgDt())
                        .build();
        }

}
