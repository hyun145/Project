package hyun.project.service.impl;


import com.fasterxml.jackson.databind.ObjectMapper;
import hyun.project.dto.MailDTO;
import hyun.project.dto.UserInfoDTO;
import hyun.project.repository.UserInfoRepository;
import hyun.project.repository.entity.UserInfoEntity;
import hyun.project.service.IMailService;
import hyun.project.service.IUserInfoService;
import hyun.project.util.CmmUtil;
import hyun.project.util.DateUtil;
import hyun.project.util.EncryptUtil;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@RequiredArgsConstructor
@Service("UserInfoService")
public class UserInfoService implements IUserInfoService {

    private final UserInfoRepository userInfoRepository;

    private final IMailService mailService;

    private EntityManager entityManager;

    @Override
    public UserInfoDTO getUserIdExists(UserInfoDTO pDTO) throws Exception {

        log.info(this.getClass().getName() +".getUserIdExists Start!!");

        UserInfoDTO rDTO;

        String userId = CmmUtil.nvl(pDTO.userId());
        log.info("userId : " + userId);

        Optional <UserInfoEntity> rEntity = userInfoRepository.findByUserId(userId);

        if(rEntity.isPresent()) {
            rDTO = UserInfoDTO.builder().existsYn("Y").build();
        }else {
            rDTO = UserInfoDTO.builder().existsYn("N").build();
        }



        log.info(this.getClass().getName() +".getUserIdExists End!!");


        return rDTO;
    }

    @Override
    public UserInfoDTO getNickNameExists(UserInfoDTO pDTO) throws Exception {

        log.info(this.getClass().getName()+".getNickNameExists Start!");

        UserInfoDTO rDTO;

        String nickName = CmmUtil.nvl(pDTO.nickName());
        log.info("nickName : " + nickName);

        Optional <UserInfoEntity> rEntity = userInfoRepository.findByNickName(nickName);

        if(rEntity.isPresent()) {
            rDTO = UserInfoDTO.builder().existsYn("Y").build();
        } else {
            rDTO = UserInfoDTO.builder().existsYn("N").build();
        }



        log.info(this.getClass().getName()+".getNickNameExists End!");

        return rDTO;
    }


    @Override
    public int insertUserInfo(UserInfoDTO pDTO) throws Exception {

        log.info(this.getClass().getName() +".insertUserInfo Start!");

        int res = 0;

        String userId = CmmUtil.nvl(pDTO.userId());
        String userName = CmmUtil.nvl(pDTO.userName());
        String password = CmmUtil.nvl(pDTO.password());
        String email = CmmUtil.nvl(pDTO.email());
        String nickName = CmmUtil.nvl(pDTO.nickName());



        log.info("pDTO : " + pDTO); // 받은 값 확인 .

        Optional<UserInfoEntity> rEntity = userInfoRepository.findByUserId(userId); // 아이디 중복체크

        if (rEntity.isPresent()) {
            res = 2;
        } else {

            UserInfoEntity pEntity = UserInfoEntity.builder()
                    .userId(userId).userName(userName)
                    .password(password)
                    .email(email)
                    .nickName(nickName)
                    .regId(userId).regDt(DateUtil.getDateTime("yyyy-MM-dd hh:mm:ss"))
                    .chgId(userId).chgDt(DateUtil.getDateTime("yyyy-MM-dd hh:mm:ss"))
                    .build();
            userInfoRepository.save(pEntity); // 회원정보 DB에 저장

            rEntity = userInfoRepository.findByUserId(userId);

            if(rEntity.isPresent()) {
                res = 1;
            } else {
                res = 0;
            }


        }
        log.info(this.getClass().getName() +"Insert.userInfo End!");

        return res;
    }

    @Override
    public UserInfoDTO getUserLogin(UserInfoDTO pDTO) throws Exception {

        log.info(this.getClass().getName() +".getUserLogin Start!");

        int res = 0;

        UserInfoDTO rDTO;
        String userId = CmmUtil.nvl(pDTO.userId());

        String password =  CmmUtil.nvl(pDTO.password());

        log.info("userID : " + userId);
        log.info("password : " + password);

        Optional<UserInfoEntity> rEntity = userInfoRepository.findByUserIdAndPassword(userId, password);
        UserInfoEntity pEntity = rEntity.get();

        if(rEntity.isPresent()) {
            rDTO =  new ObjectMapper().convertValue(pEntity, UserInfoDTO.class);

        } else {
            rDTO = UserInfoDTO.builder().build();
        }

        log.info(this.getClass().getName() +".getUserLogin End!");
        return rDTO;
    }

    @Override
    public UserInfoDTO emailCheck(UserInfoDTO pDTO) throws Exception {
        log.info(this.getClass().getName() +".emailCheck Start!");

        UserInfoDTO rDTO;
        String email = CmmUtil.nvl(pDTO.email());
        log.info("email : " + email);

        Optional <UserInfoEntity> rEntity = userInfoRepository.findByEmail(email);
        if(rEntity.isPresent()) {
            int authNumber = ThreadLocalRandom.current().nextInt(10000, 100000);

            log.info("authNumber : " + authNumber);

            MailDTO dto = MailDTO.builder()
                    .title("이메일 확인 인증번호 발송 메일")
                    .contents("인증번호는" + authNumber + "입니다.")
                    .toMail(EncryptUtil.decAES128CBC(CmmUtil.nvl(pDTO.email()))).build();



            rDTO = UserInfoDTO.builder().existsYn("Y").
                    authNumber(authNumber).build();

            mailService.doSendMail(dto);

            dto = null;

        } else {
            rDTO = UserInfoDTO.builder().existsYn("N").build();
        }

        log.info(this.getClass().getName() +".emailCheck End!");

        return rDTO;
    }

    @Override
    public void updateUserInfo(UserInfoDTO pDTO) throws Exception {

        log.info("내 정보 업데이터 서비스 시작");
        String userId = CmmUtil.nvl(pDTO.userId());
        String email = CmmUtil.nvl(EncryptUtil.encAES128CBC(pDTO.email()));
        String nickName = CmmUtil.nvl(pDTO.nickName());

        log.info("userId : " + userId);
        log.info("email : " + email);
        log.info("userName : " + nickName);

        Optional<UserInfoEntity> pEntity = userInfoRepository.findByUserId(userId);

        if(pEntity.isPresent()) {

        UserInfoEntity rEntity = UserInfoEntity.builder().email(email).nickName(nickName)
                .password(pEntity.get().getPassword())
                .userName(pEntity.get().getUserName())
                .userId(userId).build();
        userInfoRepository.save(rEntity);


        } else {
            UserInfoEntity rEntity = UserInfoEntity.builder().build();

            userInfoRepository.save(rEntity);


        }




        log.info("내 정보 업데이터 서비스 종료");

    }

    @Override
    public UserInfoDTO getMyInfo(UserInfoDTO pDTO) throws Exception {

        String userId = CmmUtil.nvl(pDTO.userId());

        Optional<UserInfoEntity> rEntity = userInfoRepository.findByUserId(userId);
        UserInfoEntity pEntity = rEntity.get();
        UserInfoDTO rDTO;

        if(rEntity.isPresent()) {
            rDTO =  new ObjectMapper().convertValue(pEntity, UserInfoDTO.class);



        } else {
            rDTO = UserInfoDTO.builder().build();
        }

        return rDTO;
    }

    @Override
    public void newPassword2(String userId, String newPassword, String email, String userName) throws Exception {

        log.info("service 비번 업데이트 실행");

        Optional<UserInfoEntity> pEntity = userInfoRepository.findByUserId(userId);
        log.info("pEntity.password : " + pEntity.get().getPassword());

        log.info("userId : " + userId + "password : " + newPassword + "email:" + email + "userName : " + userName);

        UserInfoEntity rEntity = UserInfoEntity.builder()
                .userId(userId)
                .password(newPassword)
                .userName(userName)
                .email(email)
                .nickName(pEntity.get().getNickName())
                .build();

        userInfoRepository.save(rEntity);


    }

    @Override
    public void deleteUserInfo(String userId) throws Exception {

        log.info("유저 삭제 서비스 시작");

        userInfoRepository.deleteById(userId);


        log.info("유저 삭제 서비스 종료");

    }

    @Override
    public UserInfoDTO getEmailExists(UserInfoDTO pDTO) throws Exception {

        log.info(this.getClass().getName() +".getEmailExists Start!");


        UserInfoDTO rDTO;

        String email = CmmUtil.nvl(pDTO.email());

        log.info("email : " + email);

        Optional <UserInfoEntity> rEntity = userInfoRepository.findByEmail(email);
        if(rEntity.isPresent()) {
            rDTO = UserInfoDTO.builder().existsYn("Y").build();
        } else {
            int authNumber = ThreadLocalRandom.current().nextInt(10000, 100000);

            log.info("authNumber : " + authNumber);

            MailDTO dto = MailDTO.builder()
                            .title("이메일 중복 확인 인증번호 발송 메일")
                            .contents("인증번호는" + authNumber + "입니다.")
                            .toMail(EncryptUtil.decAES128CBC(CmmUtil.nvl(pDTO.email()))).build();



            rDTO = UserInfoDTO.builder().existsYn("N").
                    authNumber(authNumber).build();

            mailService.doSendMail(dto);

            dto = null;





        }

        log.info(this.getClass().getName() +".getEmailExists End!");


        return rDTO;




    }


    @Override
    public UserInfoDTO searchPassword(UserInfoDTO pDTO) throws Exception {
        log.info(this.getClass().getName() +".searchPassword Start!");

        String userId = CmmUtil.nvl(pDTO.userId());

        String email = CmmUtil.nvl(pDTO.email());

        log.info("userId : " + userId);
        log.info("email : " + email);

        UserInfoDTO rDTO = null;

        Optional<UserInfoEntity> rEntity = userInfoRepository.findByUserIdAndEmail(userId, email);
        UserInfoEntity pEntity = rEntity.get();

        if(rEntity.isPresent()) {
            rDTO =  new ObjectMapper().convertValue(pEntity, UserInfoDTO.class);



        } else {
            rDTO = UserInfoDTO.builder().build();
        }



        log.info(this.getClass().getName() +".searchPassword End!");
        return rDTO;
    }


    @Override
    public UserInfoDTO searchUserId(UserInfoDTO pDTO) throws Exception {

        log.info(this.getClass().getName() +".searchUserIdOrPasswordProc Start!");


        String userName = CmmUtil.nvl(pDTO.userName());

        String email = CmmUtil.nvl(pDTO.email());

        log.info("userName : " + userName);
        log.info("email : " + email);



        UserInfoDTO rDTO = null;
        Optional<UserInfoEntity> rEntity = userInfoRepository.findByUserNameAndEmail(userName, email);
        UserInfoEntity pEntity = rEntity.get();
        log.info(pEntity.getUserId());

        if (rEntity.isPresent()) {

           rDTO =  new ObjectMapper().convertValue(pEntity, UserInfoDTO.class);

        } else {
            rDTO = UserInfoDTO.builder().build();
        }


        log.info(this.getClass().getName() +".searchUserIdOrPasswordProc End!");


        return rDTO;


    }

    @Override
    @Transactional
    public int newPassword(UserInfoDTO pDTO) throws Exception {
        log.info(this.getClass().getName() +"비밀번호 재설정 서비스 Start!");

        int success = userInfoRepository.updatePassword(pDTO);


        log.info(this.getClass().getName() +"비밀번호 재설정 서비스 End!");

        return success;

    }




}
