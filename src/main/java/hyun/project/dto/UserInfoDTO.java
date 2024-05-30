package hyun.project.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;

@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public record UserInfoDTO (

        String userId,

        String userName,

        String password,

        String email,

        String nickName,

        String regId,

        String regDt,

        String chgId,

        String chgDt,

        String existsYn,

        int authNumber

) {
}
