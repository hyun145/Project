package hyun.project.controller;


import hyun.project.dto.CommentDTO;
import hyun.project.dto.MsgDTO;
import hyun.project.service.ICommentService;
import hyun.project.util.CmmUtil;
import hyun.project.util.DateUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping(value = "comment")
@RestController
public class CommentController {

    private final ICommentService commentService;


    /**
     * 댓글 등록하기
     */
    @PostMapping(value = "insertComment")
    public MsgDTO insertComment(HttpServletRequest request, HttpSession session) throws Exception {

        log.info("댓글 작성 Controller 시작");

        String msg = "작성 되었습니다.";

        int res = 1;

        try
        {
            Long boardSeq = Long.valueOf(CmmUtil.nvl(request.getParameter("boardSeq")));
            String userId = CmmUtil.nvl((String) session.getAttribute("SS_USER_ID"));
            String nickname = CmmUtil.nvl((String) session.getAttribute("SS_NICK_NAME"));
            String contents = CmmUtil.nvl(request.getParameter("contents"));
            String dt = DateUtil.getDateTime("yyyy-MM-dd hh:mm:ss");
            int dept = Integer.parseInt(CmmUtil.nvl(request.getParameter("dept")));

            log.info("boardSeq : " + boardSeq);
            log.info("userId : " + userId);
            log.info("contents : " + contents);
            log.info("dt(시간) : " + dt);
            log.info("dept : " + dept);
            log.info("닉네임 : " + nickname);
            commentService.insertComment(boardSeq, userId, contents, dept, dt, nickname);

        } catch (Exception e) {
            log.info(e.toString());
            e.printStackTrace();

            msg  ="오류로 인해 실패했습니다\n다시 실행해주세요.";
            res = 0;
        }


        log.info("댓글 작성 Controller 종료");
        return MsgDTO.builder().
                result(res).msg(msg).build();
    }


    /**
     * 댓글 가져오기
     */
    @PostMapping(value = "getCommentList")
    public List<CommentDTO> getCommentList(HttpServletRequest request) throws Exception {

        log.info("댓글 리스트 가져오기 컨트롤러 시작");

//        Long boardSeq = Long.valueOf(CmmUtil.nvl(request.getParameter("nSeq")));

        Long boardSeq = Long.valueOf(CmmUtil.nvl(request.getParameter("boardSeq")));

        log.info("boardSeq : " + boardSeq);


        log.info("댓글 리스트 가져오기 컨트롤러 종료");

        return commentService.getCommentList(boardSeq);
    }



    /**
     * 댓글 수정하기
     */
    @PostMapping(value = "updateComment")
    public MsgDTO updateComment(HttpServletRequest request, HttpSession session) throws Exception {

        log.info("댓글 업데이트 컨트롤러 시작");

        Long boardSeq = Long.valueOf(CmmUtil.nvl(request.getParameter("boardSeq")));
        Long commentSeq = Long.valueOf(CmmUtil.nvl((request.getParameter("commentSeq"))));
        String userId = CmmUtil.nvl((String) session.getAttribute("SS_USER_ID"));
        String contents = CmmUtil.nvl(request.getParameter("upComment"));
        String dt = DateUtil.getDateTime("yyyy-MM-dd hh:mm:ss");
        log.info("boardSeq : " + boardSeq);

        String msg = "수정 되었습니다.";
        int res = 1;

        try {
            commentService.updateComment(boardSeq, commentSeq, userId, contents, dt);

        } catch (Exception e) {
            log.info(e.toString());
            e.printStackTrace();

            msg = "오류로 인해 실패하였습니다. \n 다시 시도해주세요.";
            res = 0;
        }

        log.info("댓글 업데이트 컨트롤러 종료");
        return MsgDTO.builder().
                msg(msg)
                .result(res).build();
    }

    @PostMapping(value = "deleteComment")
    public MsgDTO deleteComment(HttpServletRequest request, HttpSession session) throws Exception {

        log.info("댓글 삭제 컨트롤러 시작");
        Long boardSeq = Long.valueOf(CmmUtil.nvl(request.getParameter("boardSeq")));
        Long commentSeq = Long.valueOf(CmmUtil.nvl(request.getParameter("commentSeq")));
        String userId = CmmUtil.nvl((String) session.getAttribute("SS_USER_ID"));

        String msg = "삭제 되었습니다.";
        int res = 1;

        try {
            commentService.deleteComment(boardSeq, commentSeq, userId);

        } catch (Exception e) {
            log.info(e.toString());
            e.printStackTrace();

            msg = "오류로 인해 실패하였습니다.\n 다시 시도해주세요.";
            res = 0;
        }

        log.info("댓글 삭제 컨트롤러 종료");
        return MsgDTO.builder()
                .result(res)
                .msg(msg).build();
    }



}
