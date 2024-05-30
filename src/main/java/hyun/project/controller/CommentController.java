package hyun.project.controller;


import hyun.project.dto.MsgDTO;
import hyun.project.service.ICommentService;
import hyun.project.util.CmmUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RequestMapping(value = "comment")
@RestController
public class CommentController {

    private final ICommentService commentService;


    @ResponseBody
    @PostMapping(value = "insertComment")
    public MsgDTO insertcomment(HttpServletRequest request, HttpSession session) throws Exception {

        log.info("댓글 작성 컨트롤러 시작");

        String msg = "작성되었습니다.";
        int res = 1;

        try {

            String nSeq = CmmUtil.nvl(request.getParameter("nSeq"), "0");
            String nickName = CmmUtil.nvl((String) session.getAttribute("SS_NICK_NAME"));
            String comment = CmmUtil.nvl(request.getParameter("comment"));


            Long boardSeq = Long.parseLong(nSeq);

            log.info("nSeq : " + nSeq);
            log.info("nickName : " + nickName);
            log.info("comment : " + comment);

            commentService.insertComment(boardSeq, nickName, comment);
        } catch (Exception e) {

            log.info(e.toString());
            e.printStackTrace();

            msg = "오류로 인해 실패하였습니다.";
            res = 0;
        }


        log.info("댓글 저장 컨트롤러 종료");

        return MsgDTO.builder().msg(msg).result(res).build();

    }













}
