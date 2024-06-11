package hyun.project.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/grade")
public class gradeController {

    @GetMapping(value = "gradeInput")
    public String gradeInput() throws Exception {
        log.info("성적 입력 컨트롤러 시작");
        log.info("성적 입력 컨트롤러 종료");
        return "/grade/gradeInput";
    }

}
