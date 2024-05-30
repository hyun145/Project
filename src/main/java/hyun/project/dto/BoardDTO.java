package hyun.project.dto;


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

        String userName,

        String nickName,

        String fileYn




) {
}