package hyun.project.dto;

import lombok.Builder;

@Builder
public record CommentDTO
(

        int commentSeq,

        int boardSeq,

        String regId,

        String contents,

        String chgDt



)
{
}
