package hyun.project.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import hyun.project.repository.entity.BoardEntity;
import lombok.Builder;

@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public record BoardDTO(
        Long boardSeq,

        String title,

        String contents,

        String userId,

        String readCnt,

        String regId,

        String regDt,

        String chgId,

        String chgDt,

        String nickName,

        String fileYn,

        int likeCount   // 좋아요 수




) {

//    Entity -> DTO
    public static BoardDTO fromEntity(BoardEntity entity) {
        return BoardDTO.builder()
                .boardSeq(entity.getBoardSeq())
                .title(entity.getTitle())
                .contents(entity.getContents())
                .userId(entity.getUserId())
                .readCnt(String.valueOf(entity.getReadCnt()))
                .regId(entity.getRegId())
                .regDt(entity.getRegDt())
                .chgId(entity.getChgId())
                .chgDt(entity.getChgDt())
                .nickName(entity.getNickName())
                .fileYn(entity.getFileYn())
                .likeCount(entity.getLikeCount())
                .build();
    }
}