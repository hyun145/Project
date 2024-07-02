package hyun.project.dto;


import hyun.project.repository.entity.BoardEntity;
import lombok.Builder;

@Builder
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

        String fileYn




) {

//    DTO -> Entity
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
                .build();
    }
}