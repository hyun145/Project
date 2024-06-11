package hyun.project.controller;

import hyun.project.util.CmmUtil;
import jakarta.servlet.http.HttpSession;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalAdvice {



    /**
     * 반복되는 코드 최소화.
     */

    @ModelAttribute
    public void nickNameCheck(ModelMap model, HttpSession session) {
        Object ssUserId = CmmUtil.nvl((String)session.getAttribute("SS_USER_ID"));

        if ( "null".equals(ssUserId) || "".equals(ssUserId) || ssUserId == null) {
            model.addAttribute("globalSession", 0);

        } else {
            model.addAttribute("globalSession", 1);
        }


    }

}
