package hyun.project.service.impl;

import hyun.project.repository.GradeCriteriaRepository;
import hyun.project.repository.ScoreRepository;
import hyun.project.repository.entity.GradeCriteriaEntity;
import hyun.project.repository.entity.ScoreEntity;
import hyun.project.service.IGradeCriteriaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class GradeCriteriaService implements IGradeCriteriaService {

    private final GradeCriteriaRepository gradeCriteriaRepository;

    private final ScoreRepository scoreRepository;
    @Override
    public double calculateAdmissionProbability(String userId, String university, String major) throws Exception {


        log.info("계산 서비스 시작");
        log.info("서비스에서 받은 userId : " + userId);
        List<ScoreEntity> sList = scoreRepository.findByUserId(userId);
        log.info("sList : " + sList);

        if (sList.isEmpty()) {
            // 해당 userId에 대한 성적 데이터가 없는 경우 처리
            log.warn("userId {}에 대한 성적 데이터가 존재하지 않습니다.", userId);
            throw new Exception("학생의 성적 데이터가 존재하지 않습니다.");
        }

        log.info("university : " + university);
        log.info("major : " + major);
        Optional<GradeCriteriaEntity> gEntity = gradeCriteriaRepository.findBySchNameAndDepartName(university,major);

        log.info("gEntity : " + gEntity);

        // 국, 수, 영어, 역사, 제2외국어, 과학1, 과학2 순서로 성적 추출
        int korGrade = sList.get(0).getGrade();
        int mathGrade = sList.get(1).getGrade();
        int engGrade = sList.get(2).getGrade();
        int hisGrade = sList.get(3).getGrade();
        int secondLanguageGrade = sList.get(4).getGrade();
        int sciGrade = sList.get(5).getGrade();
        int sci2Grade = sList.get(6).getGrade();

        // 평균 성적 계산
        double averageGrade = (korGrade + mathGrade + engGrade + hisGrade + secondLanguageGrade + sciGrade + sci2Grade) / 7.0;
        double avgScore1 = gEntity.get().getAvgScore();

        log.info("weightedGrade : " + averageGrade);
        log.info("avgScore1 : " + avgScore1);

        // 합격률 계산 로직 수정
        double admissionProbability = 0.0;
        if (averageGrade <= avgScore1) {
            admissionProbability = 100.0;
        } else {
            // 최저, 중간, 최고 점수 가져오기
            double avgScore = gEntity.get().getAvgScore();

            // 등급별 가중치 계산 (예시: 국어 20%, 수학 20%, 영어 20%, 역사 10%, 제2외국어 10%, 과학1 10%, 과학2 10%)
            double weightedGrade = korGrade * 0.2 + mathGrade * 0.2 + engGrade * 0.2 + hisGrade * 0.1 + secondLanguageGrade * 0.1 + sciGrade * 0.1 + sci2Grade * 0.1;

            log.info("weightedGrade : " + weightedGrade);
            log.info("avgScore : " + avgScore);

            // 평균 성적이 아닌 가중치가 적용된 성적으로 합격률 계산
            if (weightedGrade <= avgScore) {
                admissionProbability = 100.0;

                log.info("1");
            } else {
                double difference = weightedGrade - avgScore;
                admissionProbability = Math.max(0, 100 - (difference * 10)); // 예: 등급 차이마다 합격률 10%씩 감소
            }
        }

        log.info("계산 서비스 종료");

        return admissionProbability;
    }
}
