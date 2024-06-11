package hyun.project.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import org.springframework.data.mongodb.core.mapping.Document;

@Builder
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
@Document(collection = "departments")
public record DepartDTO (



        String schId,

        String korMjrNm, // 학과명

        String korSchlNm,    // 학교명

        String lsnTrmNm,    // 일반/ 전문 구분

        String dghtDivNm,   // 주야 구분

        String korSrsSclftNm    // 표준 분류







)
{

}
