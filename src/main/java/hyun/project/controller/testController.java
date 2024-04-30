package hyun.project.controller;


import hyun.project.dto.MsgDTO;
import hyun.project.dto.NoticeDTO;
import hyun.project.service.INoticeService;
import hyun.project.util.CmmUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Slf4j
@RequestMapping(value = "/bootStrap")
@Controller
@RequiredArgsConstructor
public class testController {

    private final INoticeService noticeService;

    @GetMapping(value = "boardReg")
    public String boardReg() {
        log.info(this.getClass().getName() +".boardReg Start!");

        log.info(this.getClass().getName() +".boardReg End!");

        return "bootStrap/boardReg";
    }

    @ResponseBody
    @PostMapping(value = "boardInsert")
    public MsgDTO boardInsert(HttpServletRequest request, HttpSession session) {
        log.info(this.getClass().getName() +".boardInsert Start!");

        String msg = "";
        MsgDTO dto;

        try {
            String userId = CmmUtil.nvl((String) session.getAttribute("SS_USER_ID"));
            String nickName = CmmUtil.nvl((String) session.getAttribute("SS_NICK_NAME"));
            String title = CmmUtil.nvl(request.getParameter("title"));
            String contents = CmmUtil.nvl(request.getParameter("contents"));

            log.info("userId :" + userId);
            log.info("ssNickName : " + nickName);
            log.info("title : " + title);
            log.info("contents : " + contents);

            if(nickName == "null") {
                msg = "로그인 후 게시글 작성하시기 바랍니다.";
            } else {


                NoticeDTO pDTO = NoticeDTO.builder().nickName(nickName).title(title).userId(userId)
                        .contents(contents).build();

                noticeService.insertNoticeInfo(pDTO);

                msg = "등록되었습니다.";

            }



        } catch (Exception e) {
            msg = "실패하였습니다. : " + e.getMessage();
            log.info(e.toString());
            e.printStackTrace();

        } finally {

            dto = MsgDTO.builder().msg(msg).build();
        log.info(this.getClass().getName() +".boardInsert End!");
        }

        return dto;
    }


    @GetMapping(value = "index")
    public String indexForm() {
        log.info(this.getClass().getName() +".indexForm Start!");

        log.info(this.getClass().getName() +".indexForm End!");

        return "bootStrap/index";
    }


    @GetMapping(value = "boardInfo")
    public String boardInfo(HttpServletRequest request, ModelMap model, HttpSession session) throws Exception{

        log.info(this.getClass().getName() +".userGradeInputForm Start!");

        String nSeq = CmmUtil.nvl(request.getParameter("nSeq"), "0");   // HTMl로부터 전달된 NoticeSeq 값을 받기.

        String ssNickName =CmmUtil.nvl((String) session.getAttribute("SS_NICK_NAME"));



        log.info("nSeq : " + nSeq);

        NoticeDTO pDTO = NoticeDTO.builder().noticeSeq(Long.parseLong(nSeq)).build();

        NoticeDTO rDTO = Optional.ofNullable(noticeService.getNoticeInfo(pDTO, true))
                        .orElseGet(() -> NoticeDTO.builder().build());

        if (ssNickName == "null") {
        model.addAttribute("nickNameCheck", 0);
        } else {

        boolean nickNameCheck = ssNickName.equals(rDTO.nickName());

        model.addAttribute("nickNameCheck", nickNameCheck);

        }

        model.addAttribute("rDTO", rDTO);

        log.info(this.getClass().getName() +".userGradeInputForm End!");

        return "bootStrap/boardInfo";
    }
    @GetMapping(value = "boardEditInfo")
    public String boardEditInfo(HttpServletRequest request, ModelMap model) throws Exception {
        log.info(this.getClass().getName() +".boardEditInfo Start!");

        String nSeq = CmmUtil.nvl(request.getParameter("nSeq"));

        log.info("nSeq : " + nSeq);

        NoticeDTO pDTO = NoticeDTO.builder().noticeSeq(Long.parseLong(nSeq)).build();

        NoticeDTO rDTO = Optional.ofNullable(noticeService.getNoticeInfo(pDTO, false))
                        .orElseGet(() -> NoticeDTO.builder().build());

        model.addAttribute("rDTO", rDTO);
        log.info(this.getClass().getName() +".boardEditInfo End!");

        return "bootStrap/boardEditInfo";
    }
    @ResponseBody
    @PostMapping(value = "boardDelete")
    public MsgDTO boardDelete(HttpServletRequest request) {
        log.info(this.getClass().getName() +".boardDelete Start!");

        String msg = "";
        MsgDTO dto = null;

        try {
            String nSeq = CmmUtil.nvl(request.getParameter("nSeq"));

            log.info("nSeq : " + nSeq);

            NoticeDTO pDTO = NoticeDTO.builder().noticeSeq(Long.parseLong(nSeq)).build();
            noticeService.deleteNoticeInfo(pDTO);

            msg = "삭제되었습니다.";
        } catch (Exception e) {
            msg = "실패하였습니다. : " + e.getMessage();
            log.info(e.toString());
            e.printStackTrace();

        } finally {
            dto = MsgDTO.builder().msg(msg).build();

            log.info(this.getClass().getName() + ".noticeDelete End!");


        }

        return dto;
    }
    @ResponseBody
    @PostMapping(value = "boardUpdate")
    public MsgDTO boardUpdate(HttpSession session, HttpServletRequest request) {

        log.info(this.getClass().getName() +".boardUpdate Start!");

        String msg = "";
        MsgDTO dto = null;

        try {
            String userId = CmmUtil.nvl((String)session.getAttribute("SS_USER_ID"));
            String nSeq = CmmUtil.nvl(request.getParameter("nSeq"));
            String title = CmmUtil.nvl(request.getParameter("title"));
            String contents = CmmUtil.nvl(request.getParameter("contents"));
            String nickName = CmmUtil.nvl((String) session.getAttribute("SS_NICK_NAME"));
            log.info("userId : " + userId);
            log.info("nSeq : " + nSeq);
            log.info("title : " + title);
            log.info("contents : " + contents);
            log.info("nickName : " + nickName);

            NoticeDTO pDTO = NoticeDTO.builder().userId(userId).noticeSeq(Long.parseLong(nSeq))
                    .title(title).contents(contents).nickName(nickName).build();

            noticeService.updateNoticeInfo(pDTO);
            msg = "수정되었습니다.";
        } catch (Exception e) {
            msg = "실패하였습니다. : " + e.getMessage();
            log.info(e.toString());
            e.printStackTrace();
        } finally {
            dto = MsgDTO.builder().msg(msg).build();

        log.info(this.getClass().getName() +".boardUpdate End!");
        }
        return dto;
    }

    @GetMapping(value = "portfolio_details")
    public String portfolio_detailsForm() {
        log.info(this.getClass().getName() +".portfolio_detailsForm Start!");

        log.info(this.getClass().getName() +".portfolio_detailsForm End!");

        return "bootStrap/portfolio_details";
    }

    @GetMapping(value = "searchBoard")
    public String searchTitle(@RequestParam (value = "keyword") String keyWord,
                              @RequestParam (defaultValue = "1") int page, ModelMap model) throws Exception {
        log.info(this.getClass().getName() +".searchTitle Start!");


        log.info("키워드 : " + keyWord);

        NoticeDTO pDTO = NoticeDTO.builder().title(keyWord).build();    // 키워드 검색

        List<NoticeDTO> rList;

         rList = noticeService.boardSearchList(pDTO);   // 리스트에 담아두고,


       log.info("rList : " + rList);


        int itemsPerPage = 10;    // 페이지당 보여줄 게시판 개수 정의.

        int totalItems = rList.size();

        int totalPages = (int) Math.ceil((double) totalItems / itemsPerPage);

        // 현재 페이지에 해당하는 게시글만 선택하여 rList에 할당.
        int fromIndex = (page - 1) * itemsPerPage;
        int toIndex = Math.min(fromIndex + itemsPerPage, totalItems);
        rList = rList.subList(fromIndex, toIndex);

        if (rList.size() == 0) {
            rList = new ArrayList<>();
            String msg = "검색 결과가 없습니다.";
            String url = "bootStrap/mainBoard";
            model.addAttribute("msg", msg);
            model.addAttribute("url", url);

            return "/redirect";
        }


        model.addAttribute("rList", rList);

        model.addAttribute("currentPage", page);

        model.addAttribute("totalPages", totalPages);

        log.info(this.getClass().getName() +".searchTitle End!");

        return "bootStrap/mainBoard";

   }

    @GetMapping(value = "mainBoard")
    public String noticeList(@RequestParam(defaultValue = "1")int page, HttpSession session, ModelMap model)
            throws Exception {

        // 로그 찍기(추후 찍은 로그를 통해 이 함수에 접근했는지 파악하기 용이하다.)
        log.info(this.getClass().getName() + ".mainBoard Start!");

        // 로그인된 사용자 아이디는 Session에 저장함
        String ssNickName = (String) session.getAttribute("SS_NICK_NAME");
        String ssUserId = (String) session.getAttribute("SS_USER_ID");

        log.info("세션에 저장되있는 사용자 닉네임: " + ssNickName);
        log.info("세션에 저장되있는 사용자 아이디: " + ssUserId);

        // 공지사항 리스트 조회하기
        // Java 8부터 제공되는 Optional 활용하여 NPE(Null Pointer Exception) 처리
        List<NoticeDTO> rList = Optional.ofNullable(noticeService.getNoticeList())
                .orElseGet(ArrayList::new);

        if (ssNickName == "null") {
            model.addAttribute("nickNameCheck", 0);
        } else {

            model.addAttribute("nickNameCheck", 1);
        }



            // 조회된 리스트 결과값 넣어주기
        log.info("rList : " + rList);
        // 로그 찍기(추후 찍은 로그를 통해 이 함수 호출이 끝났는지 파악하기 용이하다.)
        log.info(this.getClass().getName() + ".mainBoard End!");

        // 함수 처리가 끝나고 보여줄 HTML (Thymeleaf) 파일명
        // templates/notice/noticeList.html
        int itemsPerPage = 10;    // 페이지당 보여줄 게시판 개수 정의.

        int totalItems = rList.size();  // 페이지네이션을 위한 전체 개수

        int totalPages = (int) Math.ceil((double) totalItems / itemsPerPage);


        // 현재 페이지에 해당하는 게시글만 선택하여 rList에 할당.
        int fromIndex = (page - 1) * itemsPerPage;
        int toIndex = Math.min(fromIndex + itemsPerPage, totalItems);
        rList = rList.subList(fromIndex, toIndex);

        model.addAttribute("rList", rList);


        model.addAttribute("currentPage", page);

        model.addAttribute("itemsPerPage", itemsPerPage);

        model.addAttribute("totalPages", totalPages);


        return "bootStrap/mainBoard";

    }


}

