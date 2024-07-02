package hyun.project.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import hyun.project.dto.ScoreDTO;
import hyun.project.repository.ScoreRepository;
import hyun.project.repository.entity.ScoreEntity;
import hyun.project.service.IScoreService;
import hyun.project.util.CmmUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;


@Service
@RequiredArgsConstructor
@Slf4j
public class ScoreService implements IScoreService {


    private final ScoreRepository scoreRepository;


    @Override
    @Transactional
    public void saveScore(Map<String, Object> payload, String userId) throws Exception {

        log.info("성적 저장 서비스 시작");
        String research = CmmUtil.nvl((String) payload.get("research"));


        scoreRepository.deleteByUserId(userId); // 기존 성적 삭제

        saveScores(userId, "kor_sel", Integer.parseInt((String) payload.get("kor_grade")), Integer.parseInt((String) payload.get("kor")));  // 국어 저장
        saveScores(userId, "mat_sel", Integer.parseInt((String) payload.get("mat_grade")), Integer.parseInt((String) payload.get("mat")));  // 수학 저장

        ScoreDTO eDTO = ScoreDTO.builder()
                .userId(userId)
                .subject((String) payload.get("eng"))
                .grade(Integer.parseInt((String) payload.get("eng_grade"))).build();        // 영어 저장 (과목명, 등급만 저장하면 됨.
        scoreRepository.save(eDTO.toEntity());

        ScoreDTO hDTO = ScoreDTO.builder()
                .userId(userId)
                .subject((String) payload.get("history"))
                .grade(Integer.parseInt((String) payload.get("history_grade"))).build();
        scoreRepository.save(hDTO.toEntity());

        ScoreDTO lDTO = ScoreDTO.builder()
                .userId(userId)
                .subject((String) payload.get("language_2Name"))
                .grade(Integer.parseInt((String) payload.get("language_2"))).build();
        scoreRepository.save(lDTO.toEntity());


        if ("sci".equals(research)) {   // 과학

            saveScores(userId, (String) payload.get("science1"),  Integer.parseInt((String) payload.get("science1_grade")) ,Integer.parseInt((String) payload.get("science1_score"))); //  탐구과목 1
            saveScores(userId, (String) payload.get("science2"),  Integer.parseInt((String) payload.get("science2_grade")) ,Integer.parseInt((String) payload.get("science2_score"))); //  탐구과목 2
        } else if ("soc".equals(research)) {    // 사회
            saveScores(userId, (String) payload.get("social1"),  Integer.parseInt((String) payload.get("social1_grade")) ,Integer.parseInt((String) payload.get("social1_score"))); //  탐구과목 1
            saveScores(userId, (String) payload.get("social2"),  Integer.parseInt((String) payload.get("social2_grade")) ,Integer.parseInt((String) payload.get("social2_score"))); //  탐구과목 2
        } else if ("combi".equals(research)) {  // 혼합
            saveScores(userId,  (String) payload.get("combi_sci"), Integer.parseInt((String) payload.get("combi_sci_grade")), Integer.parseInt((String) payload.get("combi_sci_score")));
            saveScores(userId,  (String) payload.get("combi_soc"), Integer.parseInt((String) payload.get("combi_soc_grade")), Integer.parseInt((String) payload.get("combi_soc_score")));
        }

        log.info("성적 저장 서비스 종료");


    }

    @Override
    public List<ScoreDTO> getScore(String userId) throws Exception {


        List<ScoreEntity> pEntity = scoreRepository.findByUserId(userId);


        log.info("pEntity : "  + pEntity.get(0).getSubject());


        ObjectMapper mapper = new ObjectMapper();
        List<ScoreDTO> nList = mapper.convertValue(pEntity, new TypeReference<List<ScoreDTO>>() {});



        log.info("nList : " + nList);

        return nList;
    }

    @Override
    public void saveScores(String userId, String subject, int grade, int standardScore) throws Exception {
        ScoreDTO scoreDTO = ScoreDTO.builder()
                .userId(userId)
                .subject(subject)
                .grade(grade)
                .standardScore(standardScore)
                // Set other fields as needed
                .build();
        scoreRepository.save(scoreDTO.toEntity());
    }





}
