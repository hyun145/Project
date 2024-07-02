package hyun.project.controller;

import hyun.project.service.IDepartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/depart")
public class DepartInfoController {

   private final IDepartService departService;
    @GetMapping(value = "getDepart")
    public String getDepart() throws Exception {
        log.info("컨트롤러 시작");

//        departService.collectDepartInfo();
        log.info("컨트롤러 종료");
        return "depart/getDepart";
    }



}
