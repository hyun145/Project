package hyun.project.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
@RequestMapping("/depart")
public class DepartInfoController {

    @GetMapping(value = "getDepart")
    public String getDepart() throws Exception {
        log.info("컨트롤러 시작");
        log.info("컨트롤러 종료");
        return "/depart/getDepart";
    }

}
