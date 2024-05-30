package hyun.project.service;

import hyun.project.dto.UserInfoDTO;

public interface IUserInfoService {

    UserInfoDTO getUserIdExists(UserInfoDTO pDTO) throws Exception;

    int insertUserInfo(UserInfoDTO pDTO) throws Exception;

    UserInfoDTO getUserLogin(UserInfoDTO pDTO) throws Exception;

    UserInfoDTO  getEmailExists(UserInfoDTO pDTO) throws Exception;


    UserInfoDTO getNickNameExists(UserInfoDTO pDTO) throws Exception;

    UserInfoDTO searchUserId(UserInfoDTO pDTO) throws Exception;

    UserInfoDTO searchPassword(UserInfoDTO pDTO) throws Exception;

    int newPassword(UserInfoDTO pDTO) throws Exception;

    UserInfoDTO emailCheck(UserInfoDTO pDTO ) throws Exception;


     void updateUserInfo(UserInfoDTO pDTO) throws Exception;



     UserInfoDTO getMyInfo(UserInfoDTO pDTO) throws Exception;

     void newPassword2(String userId, String newPassword, String email, String userName) throws Exception;

     void deleteUserInfo(String userId) throws Exception;
}
