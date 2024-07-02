package hyun.project.controller;

import hyun.project.service.IScoreService;
import hyun.project.util.CmmUtil;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/score")
public class ScoreController {

    private final IScoreService scoreService;

    @PostMapping(value = "saveScore")
    @ResponseBody
    public void submitResearch(@RequestBody Map<String, Object>payload, HttpSession session) {

        log.info("값 저장 컨트롤러 시작");
        String research = CmmUtil.nvl((String) payload.get("research")); // research 값 받아오고,
        String ssUserId = CmmUtil.nvl((String) session.getAttribute("SS_USER_ID"));

        log.info("research : " + research);
        log.info("payload : " + payload);
        log.info("ssUserId : " + ssUserId);


        try {
            scoreService.saveScore(payload, ssUserId);


        } catch (Exception e) {
            log.info(e.toString());
            e.printStackTrace();
        }


        log.info("값 저장 컨트롤러 종료");

    }



}
