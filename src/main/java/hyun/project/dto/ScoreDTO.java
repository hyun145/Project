package hyun.project.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import hyun.project.repository.entity.ScoreEntity;
import lombok.Builder;

@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public record ScoreDTO(

        Long scoreId,
        String userId,

        String subject, // 과목명

        int rawScore,   // 원점수

        int standardScore,  // 표준점수

        int percentile,     // 백분위

        int grade      // 등급


) {

    public ScoreEntity toEntity() {
        return ScoreEntity.builder()
                .userId(this.userId)
                .subject(this.subject)
                .rawScore(this.rawScore)
                .standardScore(this.standardScore)
                .percentile( this.percentile)
                .grade(this.grade)
                .build();
    }
}