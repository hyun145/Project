package hyun.project.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Slf4j
@RequiredArgsConstructor
public class MainController {

    @GetMapping(value = "main")
    public String mainPage() throws Exception {
        log.info("메인 페이지 시작");


        log.info("메인 페이지 종료");

        return "/main";
    }
}
