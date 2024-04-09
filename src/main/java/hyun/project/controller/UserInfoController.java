package hyun.project.controller;


import hyun.project.dto.MsgDTO;
import hyun.project.dto.UserInfoDTO;
import hyun.project.service.impl.UserInfoService;
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

import java.util.Optional;

@Slf4j
@RequestMapping(value = "/user")
@Controller
@RequiredArgsConstructor
public class UserInfoController {

    private final UserInfoService userInfoService;


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
        String msg;
        String user_id = CmmUtil.nvl(request.getParameter("user_id"));
        String password = CmmUtil.nvl(request.getParameter("password"));

        log.info("user_id : " + user_id);
        log.info("password : " + password);

        UserInfoDTO pDTO = UserInfoDTO.builder()
                .userId(user_id)
                .password(EncryptUtil.encHashSHA256(password))
                .build();

        int res = userInfoService.getUserLogin(pDTO);

        log.info("res : " + res);

        if (res ==1) {
            msg = "로그인이 성공했습니다.";
            session.setAttribute("SS_USER_ID",  user_id);
        } else {
            msg = "아이디와 비밀번호가 올바르지 않습니다.";
        }
        MsgDTO dto = MsgDTO.builder()
                .result(res).msg(msg).build();
        log.info(this.getClass().getName() +".loginProc End!");

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






}

