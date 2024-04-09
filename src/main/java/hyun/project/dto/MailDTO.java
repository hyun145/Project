package hyun.project.dto;


import lombok.Builder;


@Builder
public record MailDTO(


        String toMail,      // 받는사람
        String title,       // 보내는 사람 메일 제목

        String contents,        // 내용

        String mailSeq,     // 발송 순번

        String sendTime     // 발송 시간



) {

}