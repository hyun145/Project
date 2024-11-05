package hyun.project.controller;


import hyun.project.dto.BoardDTO;
import hyun.project.dto.FileDTO;
import hyun.project.dto.MsgDTO;
import hyun.project.service.IBoardService;
import hyun.project.service.IFileService;
import hyun.project.service.IS3Service;
import hyun.project.util.CmmUtil;
import hyun.project.util.FileUtil;
import hyun.project.util.SafeUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;


@Slf4j
@RequestMapping(value = "/board")
@Controller
@RequiredArgsConstructor
public class BoardController {

    private final IFileService fileService;
    private final IBoardService boardService;
    private final IS3Service s3Service;


    @GetMapping(value = "boardReg")
    public String boardReg(HttpSession session, ModelMap model) {
        log.info(this.getClass().getName() +".boardReg Start!");

        String ssUserId = (String) session.getAttribute("SS_USER_ID");

        log.info("ssUserId : " + ssUserId);

        if (ssUserId == null) {

        model.addAttribute("ssUserId", 0);
        } else {

        model.addAttribute("ssUserId", ssUserId);
        }

        log.info(this.getClass().getName() +".boardReg End!");

        return "board/boardReg";
    }

    @ResponseBody
    @PostMapping(value = "boardInsert")
    public MsgDTO boardInsert(HttpServletRequest request, HttpSession session,
                              @RequestParam(value = "file", required = false)List<MultipartFile> files) {
        log.info(this.getClass().getName() +".boardInsert Start!");
        int res = 1;
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
            BoardDTO pDTO = BoardDTO.builder()
                    .userId(userId).title(title).nickName(nickName)
                    .contents(contents).build();

            Long boardSeq = boardService.insertBoardInfo(pDTO);

            log.info("boardSeq : " + boardSeq);

            if(files != null) {
                String saveFilePath = FileUtil.mkdirForData();      // 웹서버에 저장할 파일 경로 생성

                log.info("files: " + files);

                for(MultipartFile mf : files) {

                    log.info("mf : " + mf);

                    String orgFileName = mf.getOriginalFilename();      // 파일의 원본 명
                    String fileSize = String.valueOf(mf.getSize());     // 파일 크기
                    String ext = orgFileName.substring(orgFileName.lastIndexOf(".") + 1,    // 확장자
                            orgFileName.length()).toLowerCase();
                    if (ext.equals("jpeg") || ext.equals("jpg") || ext.equals("gif") || ext.equals("png")) {

                        log.info("orgFileName : " + orgFileName);
                        log.info("fileSize : " + fileSize);
                        log.info("ext : " + ext);
                        log.info("saveFilePath : " + saveFilePath);

                        FileDTO rDTO = s3Service.uploadFile(mf, ext);

                        FileDTO fileDTO = FileDTO.builder()
                                .boardSeq(boardSeq)
                                .orgFileName(orgFileName)
                                .saveFilePath(saveFilePath)
                                .fileSize(fileSize)
                                .saveFileName(rDTO.saveFileName())
                                .saveFileUrl(rDTO.saveFileUrl())
                                .build();

                        log.info("sageFileUrl : " + rDTO.saveFileUrl());

                        fileService.saveFiles(fileDTO);

                        fileDTO = null;

                    }


                }

            }

            if( "null".equals(nickName) || "".equals(nickName) || nickName == null ) {
                msg = "로그인 후 게시글 작성하시기 바랍니다.";
                log.info("ㄱㄴㄷㄻㅄㅇ");
            }
            msg = "작성되었습니다.";


        } catch (Exception e) {
            msg = "실패하였습니다. : " + e.getMessage();
            log.info(e.toString());
            e.printStackTrace();

        } finally {

            dto = MsgDTO.builder().msg(msg).result(res).build();
        log.info(this.getClass().getName() +".boardInsert End!");
        }

        return dto;
    }


    @GetMapping(value = "index")
    public String indexForm() {
        log.info(this.getClass().getName() +".indexForm Start!");

        log.info(this.getClass().getName() +".indexForm End!");

        return "board/index";
    }


    @GetMapping(value = "boardInfo")
    public String boardInfo(HttpServletRequest request, ModelMap model, HttpSession session) throws Exception{

        log.info(this.getClass().getName() +".userGradeInputForm Start!");

        String nSeq = CmmUtil.nvl(request.getParameter("nSeq"));   // HTMl로부터 전달된 BoardSeq 값을 받기.

        String ssNickName =CmmUtil.nvl((String) session.getAttribute("SS_NICK_NAME"));



        log.info("ssNickName : " + ssNickName);

        log.info("nSeq : " + nSeq);


        BoardDTO pDTO = BoardDTO.builder().boardSeq(Long.parseLong(nSeq)).build();




        List<FileDTO> rList = fileService.getFilePath(Long.parseLong(nSeq));

        log.info("파일 경로가 담겨져있는 리스트  : " + rList);


        BoardDTO rDTO = Optional.ofNullable(boardService.getBoardInfo(pDTO, true))
                        .orElseGet(() -> BoardDTO.builder().build());

        String nik = CmmUtil.nvl(rDTO.nickName());
        log.info("nik : " + nik);


        if (ssNickName.equals(rDTO.nickName())) {
            model.addAttribute("nk", 1);
            log.info("일로 들어옴.");
        } else {
            model.addAttribute("nk", 0);
        }


        if (ssNickName == "null" || ssNickName == null) {
        model.addAttribute("nickNameCheck", 0);
        } else {

        boolean nickNameCheck = ssNickName.equals(rDTO.nickName());

        model.addAttribute("nickNameCheck", 1);

        }

        log.info("rDTO : " + rDTO);

        model.addAttribute("rDTO", rDTO);

        model.addAttribute("rList", rList);

        log.info(this.getClass().getName() +".게시글 상세보기 컨트롤러 End!");

        return "board/boardInfo";
    }
    @GetMapping(value = "boardEditInfo")
    public String boardEditInfo(HttpServletRequest request, ModelMap model) throws Exception {
        log.info(this.getClass().getName() +".boardEditInfo Start!");

        String nSeq = CmmUtil.nvl(request.getParameter("nSeq"));

        log.info("nSeq : " + nSeq);

        BoardDTO pDTO = BoardDTO.builder().boardSeq(Long.parseLong(nSeq)).build();

        BoardDTO rDTO = Optional.ofNullable(boardService.getBoardInfo(pDTO, false))
                        .orElseGet(() -> BoardDTO.builder().build());

        model.addAttribute("rDTO", rDTO);
        log.info(this.getClass().getName() +".boardEditInfo End!");

        return "board/boardEditInfo";
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

            BoardDTO pDTO = BoardDTO.builder().boardSeq(Long.parseLong(nSeq)).build();
            boardService.deleteBoardInfo(pDTO);

            msg = "삭제되었습니다.";
        } catch (Exception e) {
            msg = "실패하였습니다. : " + e.getMessage();
            log.info(e.toString());
            e.printStackTrace();

        } finally {
            dto = MsgDTO.builder().msg(msg).build();

            log.info(this.getClass().getName() + ".boardDelete End!");


        }

        return dto;
    }
    @ResponseBody
    @PostMapping(value = "boardUpdate")
    public MsgDTO boardUpdate(HttpSession session, HttpServletRequest request,
                              @RequestParam(value = "file", required = false)List<MultipartFile> files)throws Exception {

        log.info(this.getClass().getName() +".boardUpdate Start!");

//        업데이트 할 떄 파일 확인후 , 있다면 처리.
        String msg = "";
        MsgDTO dto = null;
        int res = 1;

        try {
            String userId = CmmUtil.nvl((String)session.getAttribute("SS_USER_ID"));
            Long boardSeq = Long.valueOf(CmmUtil.nvl(request.getParameter("nSeq")));
            String title = CmmUtil.nvl(request.getParameter("title"));
            String contents = CmmUtil.nvl(request.getParameter("contents"));
            String nickName = CmmUtil.nvl((String) session.getAttribute("SS_NICK_NAME"));
            log.info("userId : " + userId);
            log.info("boardSeq : " + boardSeq);
            log.info("title : " + title);
            log.info("contents : " + contents);
            log.info("nickName : " + nickName);






            if (files != null) {

                BoardDTO pDTO = BoardDTO.builder().userId(userId).boardSeq(boardSeq)
                        .title(title).contents(contents).nickName(nickName).fileYn("Y").build();
                res = boardService.updateBoardInfo(pDTO);

                String saveFilePath = FileUtil.mkdirForData();

                for (MultipartFile mf : files) {

                    log.info("mf : " + mf);

                    String orgFileName = mf.getOriginalFilename();  // 원본명
                    String fileSize = String.valueOf(mf.getSize());
                    String ext = orgFileName.substring(orgFileName.lastIndexOf(".") + 1,
                            orgFileName.length()).toLowerCase();        // 확장자

                    if (ext.equals("jpeg") || ext.equals("jpg") || ext.equals("gif") || ext.equals("png")) {

                        log.info("orgFileName : "+ orgFileName);
                        log.info("fileSize : " + fileSize);
                        log.info("ext : " + ext);
                        log.info("saveFilePath : " + saveFilePath);

                        FileDTO rDTO = s3Service.uploadFile(mf, ext);

                        FileDTO fDTO = FileDTO.builder().
                                boardSeq(boardSeq)
                                .orgFileName(orgFileName)
                                .saveFilePath(saveFilePath)
                                .fileSize(fileSize)
                                .saveFileName(rDTO.saveFileName())
                                .saveFileUrl((rDTO.saveFileUrl()))
                                .build();

                        log.info("saveFileUrl : " + rDTO.saveFileUrl());

                        fileService.deleteFiles(boardSeq);

                        fileService.saveFiles(fDTO);

                        fDTO = null;

                    }
                }

            } else {
                BoardDTO pDTO = BoardDTO.builder().userId(userId).boardSeq(boardSeq)
                        .title(title).contents(contents).nickName(nickName).fileYn("N").build();
                res = boardService.updateBoardInfo(pDTO);
            }

            msg = "수정되었습니다.";
        } catch (Exception e) {
            msg = "실패하였습니다. : " + e.getMessage();
            log.info(e.toString());
            e.printStackTrace();
        } finally {
            dto = MsgDTO.builder().msg(msg).
                result(res).build();

        log.info(this.getClass().getName() +".boardUpdate End!");
        }
        return dto;
    }



    @GetMapping(value = "mainBoard")
    public String mainBoard() {
        return "board/mainBoard";
    }
    @ResponseBody
    @PostMapping("getBoardList")
    public Page<BoardDTO> getBoardList(HttpServletRequest request) throws Exception {
        log.info("게시글 리스트 가져오기 컨트롤러 시작");

        String keyword = CmmUtil.nvl(request.getParameter("keyword"));
        int page = SafeUtil.safeParseInt(  // 값이 없을 경우 기본값으로 0을 사용
                request.getParameter("page"), 0
        );

        log.info("keyword : " + keyword);
        log.info("page : " + page);


        log.info("게시글 리스트 가져오기 컨트롤러 종료");
        return boardService.getBoardList(
                PageRequest.of(page-2, 10), keyword);
    }






}

