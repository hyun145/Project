package hyun.project.controller;


import hyun.project.controller.response.CommonResponse;
import hyun.project.dto.DepartDTO;
import hyun.project.service.IDepartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


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
    public ResponseEntity collectDepartInfo() throws Exception {

        log.info("몽고디비 리스트 조회 컨트롤러 시작 ");

        List<DepartDTO> rList;
        int res = departService.collectDepartInfo();

        if (res == 1) {

        rList = Optional.ofNullable(departService.getDepartInfoTest())
                .orElseGet(ArrayList::new);
        } else {
            rList = new ArrayList<>();
        }



        log.info("몽고디비 리스트 조회 컨트롤러 종료 ");

        return ResponseEntity.ok(
                CommonResponse.of(HttpStatus.OK, HttpStatus.OK.series().name(), rList)
        );


    }



}
