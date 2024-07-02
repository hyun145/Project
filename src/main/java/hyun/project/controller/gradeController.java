package hyun.project.controller;

import hyun.project.dto.ScoreDTO;
import hyun.project.service.IGradeCriteriaService;
import hyun.project.service.IScoreService;
import hyun.project.util.CmmUtil;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/grade")
public class gradeController {

    private final IGradeCriteriaService gradeCriteriaService;

    private final IScoreService scoreService;
    @GetMapping(value = "gradeInput")
    public String gradeInput() throws Exception {
        log.info("성적 입력 컨트롤러 시작");
        log.info("성적 입력 컨트롤러 종료");
        return "grade/gradeInput";
    }


    @GetMapping(value = "gradeRes")
    public String gradePredic(HttpSession session) throws Exception {
        log.info("합격률 예측 컨트롤러 시작");


        String ssUserId = CmmUtil.nvl((String) session.getAttribute("SS_USER_ID"));

        log.info("합격률 예측 컨트롤러 종료");

        return "grade/gradeRes";
    }

    @GetMapping(value = "gradeResponse")
    @ResponseBody
    public ResponseEntity<Double> predictAdmission(HttpSession session,
                                                   @RequestParam String university,
                                                   @RequestParam String major ) {

        log.info("계산 컨트롤러 시작");


        log.info("선택한 학교명 : " + university);
        log.info("선택한 학교의 학과명 : " + major);
        double probability = 0.0;
        String ssUserId = CmmUtil.nvl((String) session.getAttribute("SS_USER_ID"));

        try {
            probability = gradeCriteriaService.calculateAdmissionProbability(ssUserId, university, major);
            log.info("probability :" + probability );
        }catch (Exception e) {

            log.info(e.toString());
            e.printStackTrace();

        }

        log.info("계산 컨트롤러 종료");


        return ResponseEntity.ok(probability);

    }

    private double getAdmissionRate(List<ScoreDTO> rList) {
        RestTemplate restTemplate = new RestTemplate();     // Post 요청을 보내고, PythonAPI부터 합격률을 받아온다.
        String pythonApiUrl = "http://localhost:5000/predict";

        for (ScoreDTO score : rList) {
            Map<String, Object> request = new HashMap<>();
            request.put("competition_rate", score.standardScore());
            Map<String, Object> response = restTemplate.postForObject(pythonApiUrl, request, Map.class);

            return (double) response.get("합격률");
        }
        return 0.0;
    }


}
