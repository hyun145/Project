package hyun.project.controller;


import hyun.project.dto.BoardDTO;
import hyun.project.dto.MsgDTO;
import hyun.project.dto.UserInfoDTO;
import hyun.project.service.IBoardService;
import hyun.project.service.IUserInfoService;
import hyun.project.util.CmmUtil;
import hyun.project.util.EncryptUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequestMapping(value = "/user")
@Controller
@RequiredArgsConstructor
public class UserInfoController {

    private final IUserInfoService userInfoService;
    private final IBoardService boardService;


    /**
     * 내 정보 수정 페이지
     */
    @GetMapping(value = "myPage")
    public String myPage(HttpSession session, RedirectAttributes redirectAttributes,
                         ModelMap model) {
        log.info(this.getClass().getName() +".마이페이지 Start!");

        String ssUserId = CmmUtil.nvl((String)session.getAttribute("SS_USER_ID"));
        log.info("ssUserId:" + ssUserId);

        try {

        List<BoardDTO> rList = Optional.ofNullable(boardService.findByBoardByUserId(ssUserId))
                .orElseGet(ArrayList::new);

        model.addAttribute("rList", rList);

        } catch (Exception e) {
            log.info(e.toString());
            e.printStackTrace();
        }


        log.info(this.getClass().getName() +".마이페이지 End!");

        return "user/myPage";
    }



    @ResponseBody
    @PostMapping(value = "updateUserInfo")
    public MsgDTO updateUserInfo(HttpServletRequest request, HttpSession session) throws Exception {

        log.info(this.getClass().getName() +". 내 정보 업데이트 컨트롤러 시작");

        String userId = CmmUtil.nvl((String)session.getAttribute("SS_USER_ID"));

        String email = CmmUtil.nvl(request.getParameter("email"));
        String nickName = CmmUtil.nvl(request.getParameter("nickName"));

        UserInfoDTO pDTO;

        log.info("세션 유저 아이디 : " + userId);
        log.info("email : " + email );
        log.info("유저 이름 : " + nickName);

        int res = 0;
        String msg = "";

        try {

            pDTO = UserInfoDTO.builder().       // Password가 Null이면 안되는데 , Null 값이 찍힐거임. 그래서 에러 발생,
                    userId(userId).email(email) // DTO 형식으로 하는 건 별로 ?
                    .nickName(nickName).build();

            userInfoService.updateUserInfo(pDTO);
            res = 1;
            msg = "수정되었습니다.";
        } catch (Exception e) {
            log.info(e.toString());
            e.printStackTrace();
            msg = "오류로 인해 실패하였습니다.";
        }


        log.info(this.getClass().getName() +". 내 정보 업데이트 컨트롤러 종료");

        return MsgDTO.builder().result(res).msg(msg).build();

    }

    /**
     * 내 정보 수정 페이지 상세
     */
    @GetMapping(value = "myPageInfo")
    public String myPageInfo(ModelMap model, HttpSession session) throws Exception {

        log.info("내 정보 가져오기 컨트롤러 시작");

        String userId = CmmUtil.nvl((String) session.getAttribute("SS_USER_ID"));
        UserInfoDTO pDTO = UserInfoDTO.builder().userId(userId).build();


        UserInfoDTO rDTO = Optional.ofNullable(userInfoService.getMyInfo(pDTO))
                .orElseGet(() -> UserInfoDTO.builder().build());

        log.info("userId : " + userId);
        log.info("rDTO : " + rDTO);
        model.addAttribute("rDTO", rDTO);


        log.info("내 정보 가져오기 컨트롤러 종료");
        return "user/myPageInfo";
    }




    @GetMapping(value = "userRegForm")
    public String userRegForm() {
        log.info(this.getClass().getName() + ".user/userRegForm Start!");

        log.info(this.getClass().getName() + ".user/userRegForm End!");

        return "user/userRegForm";
    }

    @ResponseBody
    @PostMapping(value = "getUserIdExists")
    public UserInfoDTO getUserIdExists(HttpServletRequest request) throws Exception {

        log.info(this.getClass().getName() + ".getUSerIdExists Start!");
        String userId = CmmUtil.nvl(request.getParameter("userId"));
        log.info("userId : " + userId);

        UserInfoDTO pDTO = UserInfoDTO.builder().userId(userId).build();

        UserInfoDTO rDTO = Optional.ofNullable(userInfoService.getUserIdExists(pDTO))
                .orElseGet(() -> UserInfoDTO.builder().build());


        log.info(this.getClass().getName() + ".getUSerIdExists End!!");

        return rDTO;
    }

    @ResponseBody
    @PostMapping(value = "getNickNameExists")
    public UserInfoDTO getNickNameExists(HttpServletRequest request) throws Exception {

        log.info(this.getClass().getName() + ".getNickNameExists Start!");
        String nickName = CmmUtil.nvl(request.getParameter("nickName"));
        log.info("nickName : " + nickName);

        UserInfoDTO pDTO = UserInfoDTO.builder().nickName(nickName).build();

        UserInfoDTO rDTO = Optional.ofNullable(userInfoService.getNickNameExists(pDTO))
                .orElseGet(() -> UserInfoDTO.builder().build());


        log.info(this.getClass().getName() + ".getNickNameExists End!!");

        return rDTO;
    }

    @ResponseBody       // Ajax로 부터 호출함.
    @PostMapping(value = "insertUserInfo")
    public MsgDTO insertUserInfo(HttpServletRequest request) throws Exception {

        log.info(this.getClass().getName() + ".insertUserInfo Start!");

        String msg;

        String userId = CmmUtil.nvl(request.getParameter("userId"));
        String userName = CmmUtil.nvl(request.getParameter("userName"));
        String password = CmmUtil.nvl(request.getParameter("password"));
        String email = CmmUtil.nvl(request.getParameter("email"));
        String nickName = CmmUtil.nvl(request.getParameter("nickName"));

        log.info("userId : " + userId);
        log.info("userName : " + userName);
        log.info("password : " + password);
        log.info("email : " + email);
        log.info("nickName : " + nickName);


        UserInfoDTO pDTO = UserInfoDTO.builder()
                .userId(userId)
                .userName(userName)
                .password(EncryptUtil.encHashSHA256(password))
                .email(EncryptUtil.encAES128CBC(email))
                .nickName(nickName)
                .regId(userId)
                .chgId(userId).build();

        int res = userInfoService.insertUserInfo(pDTO);

        log.info("회원가입 결과 (res) : " + res);


        if (res == 1) {

            msg = "회원가입되었습니다.";

        } else if (res == 2) {
            msg = "이미 가입된 아이디입니다.";

        } else {
            msg = "오류로 인해 회원가입이 실패하였습니다.";


        }
        MsgDTO dto = MsgDTO.builder().result(res).msg(msg).build();


        log.info(this.getClass().getName() + ".insertUserInfo End!");

        return dto;
    }

    @GetMapping(value = "login")
    public String login() {
        log.info(this.getClass().getName() +".login Start!");
        log.info(this.getClass().getName() +".login End!");


        return "user/login";
    }

    @ResponseBody
    @PostMapping (value = "loginProc")
    public MsgDTO loginProc(HttpServletRequest request, HttpSession session) throws Exception {
        log.info(this.getClass().getName() +".loginProc Start!");
        int res = 0;
        String msg = "";
        MsgDTO dto = null;

        try {

        String userId = CmmUtil.nvl(request.getParameter("userId"));
        String password = CmmUtil.nvl(request.getParameter("password"));

        log.info("userId : " + userId);
        log.info("password : " + password);

        UserInfoDTO pDTO = UserInfoDTO.builder()
                .userId(userId)
                .password(EncryptUtil.encHashSHA256(password))
                .build();

        UserInfoDTO rDTO = Optional.ofNullable(userInfoService.getUserLogin(pDTO))
                .orElseGet(() -> UserInfoDTO.builder().build());

            if (CmmUtil.nvl(rDTO.userId()).length() > 0) {
                res = 1;

                session.setAttribute("SS_NICK_NAME", CmmUtil.nvl(rDTO.nickName()));
                session.setAttribute("SS_USER_ID", CmmUtil.nvl(rDTO.userId()));

                log.info("일로 잘 들어옴.");
            }
        } catch (Exception e) {
            log.info("catch로 들어옴. ");
            msg = "아이디와 비밀번호가 일치하지 않습니다.";
            res = 2;
            log.info(e.toString());
            e.printStackTrace();
        } finally {
            dto = MsgDTO.builder().msg(msg).result(res).build();
            log.info(this.getClass().getName() +".loginProc End!");
        }
        return dto;
    }

    @GetMapping(value = "loginSuccess")
    public String loginSuccess() {
        log.info(this.getClass().getName() +".user/loginSuccess Start!");

        log.info(this.getClass().getName() +".user/loginSuccess End!");

        return "user/loginSuccess";
    }
    @ResponseBody
    @PostMapping(value = "logout")
    public MsgDTO logout(HttpSession session) {
        log.info(this.getClass().getName() +".logout Start!");

        session.setAttribute("SS_USER_ID", "");
        session.removeAttribute("SS_USER_ID");

        session.setAttribute("SS_NICK_NAME", "");
        session.removeAttribute("SS_NICK_NAME");

        MsgDTO dto = MsgDTO.builder().msg("로그아웃하였습니다.").result(1).build();


        log.info(this.getClass().getName() +".logout End!");

        return dto;
    }


    @ResponseBody
    @PostMapping(value = "getEmailExists")
    public UserInfoDTO getEmailExists(HttpServletRequest request) throws Exception {
        log.info( this.getClass().getName() + ".getEmailExists Start!");

        String email = CmmUtil.nvl(request.getParameter("email"));

        log.info("email : " + email);

        UserInfoDTO pDTO = UserInfoDTO.builder().
                                        email(EncryptUtil.encAES128CBC(email)).build();
         UserInfoDTO rDTO = Optional.ofNullable(userInfoService.getEmailExists(pDTO))
                         .orElseGet(() -> UserInfoDTO.builder().build());


        log.info( this.getClass().getName() + ".getEmailExists End!");

        return rDTO;
    }

    @ResponseBody
    @PostMapping(value = "emailCheck")
    public UserInfoDTO emailCheck(HttpServletRequest request) throws Exception {
        log.info( this.getClass().getName() + ".emailCheck Start!");

        String email = CmmUtil.nvl(request.getParameter("email"));

        log.info("email : " + email);

        UserInfoDTO pDTO = UserInfoDTO.builder().
                email(EncryptUtil.encAES128CBC(email)).build();
        UserInfoDTO rDTO = Optional.ofNullable(userInfoService.emailCheck(pDTO))
                .orElseGet(() -> UserInfoDTO.builder().build());


        log.info( this.getClass().getName() + ".emailCheck End!");

        return rDTO;
    }



    @GetMapping(value = "userRegTest")
    public String index() {
        log.info(this.getClass().getName() +".index Start!");
        log.info(this.getClass().getName() +".index End!");

        return "userRegTestForm";
    }

    @GetMapping(value = "searchUserId")
    public String searchUserId() {
        log.info(this.getClass().getName() +".searchUserId Start!");
        log.info(this.getClass().getName() +".searchUserId End!");
        return "/user/searchUserId";

    }


    /**
     * 아이디 찾기
     */
    @ResponseBody
    @PostMapping(value = "searchUserIdProc")
    public MsgDTO searchUserIdProc(HttpServletRequest request, ModelMap model) throws Exception {
        log.info(this.getClass().getName() +".searchUserIdProc Start!");

        int res = 0;

        UserInfoDTO pDTO;

        MsgDTO dto = null;

        String msg = "";

        try {

            String userName = CmmUtil.nvl(request.getParameter("userName"));
            String email = CmmUtil.nvl(request.getParameter("email"));

            log.info("userName : " + userName);
            log.info("email : " + email);


            pDTO = UserInfoDTO.builder().
            userName(userName).email(EncryptUtil.encAES128CBC(email)).build();

            UserInfoDTO rDTO = Optional.ofNullable(userInfoService.searchUserId(pDTO))
                    .orElseGet(() -> UserInfoDTO.builder().build());

            log.info("rDTO : " + rDTO);





            msg = "회원님의 아이디는" + CmmUtil.nvl(rDTO.userId()) + "입니다.";

        }catch (Exception e) {
            msg = "아이디 또는 이메일을 확인하세요.";
        } finally {

            dto = MsgDTO.builder()
                    .result(res).
                    msg(msg).build();
        }

        return dto;

    }



    @GetMapping(value = "searchPassword")
    public String searchPassword(HttpSession session) {
        log.info(this.getClass().getName() +".searchPassword Start!");
        session.setAttribute("NEW_PASSWORD", "");
        session.removeAttribute("NEW_PASSSWORD");

        log.info(this.getClass().getName() +".searchPassword End!");
        return "/user/searchPassword";
    }

    /**
     * 비밀번호 찾기
     */

    @PostMapping(value = "searchPasswordProc")
    public String searchPasswordProc(HttpServletRequest request, ModelMap model, HttpSession session) throws Exception {

        log.info(this.getClass().getName() +".searchPasswordProc Start!");


        String userId = CmmUtil.nvl(request.getParameter("userId"));

        String email=  CmmUtil.nvl(request.getParameter("email"));

        log.info("userId : " + userId);
        log.info("email : " + email);

        UserInfoDTO pDTO = UserInfoDTO.builder().userId(userId)
                        .email(EncryptUtil.encAES128CBC(email)).build();

        UserInfoDTO rDTO = Optional.ofNullable(userInfoService.searchPassword(pDTO))
                        .orElseGet(() -> UserInfoDTO.builder().build());


        model.addAttribute("rDTO", rDTO);

        session.setAttribute("NEW_PASSWORD", userId);       // userId를 넣어서 비밀번호 재설정하는 newPasswordProc 함수에서 사용하기 위함임.
        log.info(this.getClass().getName() +".searchPasswordProc End!");

        return "user/newPassword";
    }


    /**
     * 로그인창 비밀번호 재설정
     */
    @PostMapping(value = "newPasswordProc")
    public String newPasswordProc(HttpServletRequest request, ModelMap model, HttpSession session) throws Exception {

        log.info(this.getClass().getName() +".newPasswordProc Start!");

        String msg = "";

        String newPassword = CmmUtil.nvl((String) session.getAttribute("NEW_PASSWORD"));

        if(newPassword.length() >0) {
            String password = CmmUtil.nvl(request.getParameter("password"));

            log.info("입력한 새로운 password : " + password);
            UserInfoDTO pDTO = UserInfoDTO.builder()
                    .userId(newPassword).password(EncryptUtil.encHashSHA256(password)).build();

            userInfoService.newPassword(pDTO);

            session.setAttribute("NEW_PASSWORD", "");
            session.removeAttribute("NEW_PASSWORD");

            msg = "비밀번호가 재설정되었습니다.";

        } else {
            msg = "비정상 접근입니다.";
        }
        model.addAttribute("msg", msg);

        log.info(this.getClass().getName() +".newPasswordProc End!");
        return "/user/login";
    }

    /**
     * 마이페이지 비밀번호 재설정
     */
    @ResponseBody
    @PostMapping(value = "updatePassword")
    public MsgDTO updatePassword(HttpSession session, HttpServletRequest request) throws Exception {
        log.info("마이페이지 비밀번호 업데이트 컨트롤러 시작");

        int res = 0;
        String msg = "";
        try {


        String userId = CmmUtil.nvl((String) session.getAttribute("SS_USER_ID"));
        String email = CmmUtil.nvl(EncryptUtil.encAES128CBC(request.getParameter("email")));
        String password = CmmUtil.nvl(EncryptUtil.encHashSHA256(request.getParameter("password")));
        String newPassword = CmmUtil.nvl(EncryptUtil.encHashSHA256(request.getParameter("newPassword")));

        log.info("userId : " + userId);
        log.info("email : " + email);
        log.info("password : " + password);
        log.info("newPassword : " + newPassword);

        UserInfoDTO pDTO = UserInfoDTO.builder().userId(userId).email(email)
                .password(password).build();



          UserInfoDTO rDTO =Optional.ofNullable(userInfoService.getUserLogin(pDTO))
                .orElseGet(() -> UserInfoDTO.builder().build());


          if (rDTO.userId().equals(userId) || rDTO.password().equals(password)) {
              userInfoService.newPassword2(userId, newPassword, email, rDTO.userName());
              msg = "수정되었습니다.";
              res = 1;
          }


        } catch (Exception e) {
            res = 0;
            msg = "입력하신 정보가 일치하지 않습니다. 다시 확인해주세요.";
        }

        log.info("마이페이지 비밀번호 업데이트 컨트롤러 종료");
        return MsgDTO.builder().result(res).msg(msg).build();
    }



    @ResponseBody
    @PostMapping(value = "deleteUserInfo")
    public MsgDTO deleteUserInfo(HttpSession session) throws Exception {

        log.info("회원 탈퇴 컨트롤러 시작");

        String userId = CmmUtil.nvl((String)session.getAttribute("SS_USER_ID"));

        log.info("세션에 저장 되있는 유저 아이디 : " + userId);


        boardService.deleteBoardByUserId(userId);
        userInfoService.deleteUserInfo(userId);




        log.info("회원 탈퇴 컨트롤러 종료");

        return MsgDTO.builder().
                msg("탈퇴 완료하였습니다.")
                .result(1).build();
    }



}

