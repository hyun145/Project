package hyun.project.controller;


import hyun.project.dto.MailDTO;
import hyun.project.dto.MsgDTO;
import hyun.project.service.IMailService;
import hyun.project.util.CmmUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Slf4j
@RequestMapping(value = "/mail")
@Controller
@RequiredArgsConstructor
public class MailController {


    private final IMailService mailService;


    // 메일 발송하기 폼.
    @GetMapping(value = "mailForm")
    public String mailForm() throws Exception {

        log.info(this.getClass().getName()+".mailForm Start!");

        log.info(this.getClass().getName()+".mailForm End!");
        return "mail/mailForm";


    }

    // 메일 발송하기.
    @ResponseBody
    @PostMapping(value = "sendMail")
    public MsgDTO sendMail(HttpServletRequest request, ModelMap model) throws Exception {

        log.info(this.getClass().getName() +".sendMail Start!");

        String msg = "";

        String toMail = CmmUtil.nvl(request.getParameter("toMail"));

        String title = CmmUtil.nvl(request.getParameter("title"));

        String contents = CmmUtil.nvl(request.getParameter("contents"));


        MailDTO pDTO = MailDTO.builder().
                toMail(toMail).title(title)
                .contents(contents).build();


        int res = mailService.doSendMail(pDTO);

        if (res ==1) {

            msg = "메일 발송 실패하였습니다.";
        }

        log.info(msg);



        MsgDTO dto = MsgDTO.builder().
                msg(msg).build();



        log.info(this.getClass().getName() +".sendMail End!");


        return dto;
    }



}
