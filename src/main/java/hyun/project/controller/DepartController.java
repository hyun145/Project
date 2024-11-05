package hyun.project.controller;


import hyun.project.controller.response.CommonResponse;
import hyun.project.dto.DepartDTO;
import hyun.project.service.IDepartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/depart")
public class DepartController {

    private final IDepartService departService;



    /**
     * 1번 저장하고,
     * 조회 하면 됨 .
     */
    @PostMapping(value = "departInfo")
    public ResponseEntity collectDepartInfo(ModelMap model,
                                            @RequestParam(defaultValue = "0")int page,
                                            @RequestParam(defaultValue = "20")int size,
                                            @RequestParam(required = false)String query) throws Exception {

        log.info("몽고디비 리스트 조회 컨트롤러 시작 ");


        Page<DepartDTO> rList = departService.getDepartInfoList(PageRequest.of(page, size), query);

//        departService.collectDepartInfo();

        model.addAttribute("rList", rList.getContent());


        log.info("몽고디비 리스트 조회 컨트롤러 종료 ");

        return ResponseEntity.ok(
                CommonResponse.of(HttpStatus.OK, HttpStatus.OK.series().name(), rList)
        );


    }



}
