package hyun.project.controller;


import hyun.project.dto.BoardDTO;
import hyun.project.dto.CommentDTO;
import hyun.project.dto.FileDTO;
import hyun.project.dto.MsgDTO;
import hyun.project.service.IBoardService;
import hyun.project.service.ICommentService;
import hyun.project.service.IFileService;
import hyun.project.service.IS3Service;
import hyun.project.util.CmmUtil;
import hyun.project.util.FileUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
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
    private final ICommentService commentService;

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

        String nSeq = CmmUtil.nvl(request.getParameter("nSeq"), "0");   // HTMl로부터 전달된 BoardSeq 값을 받기.

        String ssNickName =CmmUtil.nvl((String) session.getAttribute("SS_NICK_NAME"));

        log.info("ssNickName : " + ssNickName);

        log.info("nSeq : " + nSeq);


        BoardDTO pDTO = BoardDTO.builder().boardSeq(Long.parseLong(nSeq)).build();

        CommentDTO cDTO = CommentDTO.builder()
                .boardSeq(Long.parseLong(nSeq)).build();


        List<FileDTO> rList = fileService.getFilePath(Long.parseLong(nSeq));

        log.info("파일 경로가 담겨져있는 리스트  : " + rList);


        BoardDTO rDTO = Optional.ofNullable(boardService.getBoardInfo(pDTO, true))
                        .orElseGet(() -> BoardDTO.builder().build());

        List<CommentDTO> cList = Optional.ofNullable(commentService.getCommentList(cDTO))
                        .orElseGet(ArrayList::new);



        if (ssNickName == "null" || ssNickName == null) {
        model.addAttribute("nickNameCheck", 0);
        } else {

        boolean nickNameCheck = ssNickName.equals(rDTO.nickName());

        model.addAttribute("nickNameCheck", 1);

        }

        model.addAttribute("rDTO", rDTO);

        model.addAttribute("rList", rList);



        model.addAttribute("cList", cList);

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


    @GetMapping(value = "searchBoard")
    public String searchTitle(@RequestParam (value = "keyword") String keyWord,
                              @RequestParam (defaultValue = "1") int page, ModelMap model) throws Exception {
        log.info(this.getClass().getName() +".searchTitle Start!");


        log.info("키워드 : " + keyWord);

        BoardDTO pDTO = BoardDTO.builder().title(keyWord).build();    // 키워드 검색

        List<BoardDTO> rList;
         rList = boardService.boardSearchList(pDTO);   // 리스트에 담아두고,


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
            String url = "board/mainBoard?page=1";
            model.addAttribute("msg", msg);
            model.addAttribute("url", url);

            return "redirect:/board/mainBoard";
        }

        model.addAttribute("totalItems", totalItems);

        model.addAttribute("rList", rList);

        model.addAttribute("rList", rList);

        model.addAttribute("currentPage", page);

        model.addAttribute("totalPages", totalPages);

        log.info(this.getClass().getName() +".searchTitle End!");

        return "board/mainBoard";

   }

    @GetMapping(value = "mainBoard")
    public String boardList(@RequestParam(defaultValue = "1")int page, HttpSession session, ModelMap model)
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
        List<BoardDTO> rList = Optional.ofNullable(boardService.getBoardList())
                .orElseGet(ArrayList::new);

        if ( "null".equals(ssNickName) || "".equals(ssNickName) || ssNickName == null) {
            model.addAttribute("nickNameCheck", 0);

        } else {

            model.addAttribute("nickNameCheck", 1);
        }



            // 조회된 리스트 결과값 넣어주기
        log.info("rList : " + rList);
        // 로그 찍기(추후 찍은 로그를 통해 이 함수 호출이 끝났는지 파악하기 용이하다.)

        // 함수 처리가 끝나고 보여줄 HTML (Thymeleaf) 파일명
        int itemsPerPage = 10;    // 페이지당 보여줄 게시판 개수 정의.

        int totalItems = rList.size();  // 페이지네이션을 위한 전체 개수

        int totalPages = (int) Math.ceil((double) totalItems / itemsPerPage);


        // 현재 페이지에 해당하는 게시글만 선택하여 rList에 할당.
        int fromIndex = (page - 1) * itemsPerPage;
        int toIndex = Math.min(fromIndex + itemsPerPage, totalItems);
        rList = rList.subList(fromIndex, toIndex);

        model.addAttribute("rList", rList);

        model.addAttribute("currentPage", page);

        model.addAttribute("totalPages", totalPages);

        log.info(this.getClass().getName() + ".mainBoard End!");

        return "board/mainBoard";

    }





}

