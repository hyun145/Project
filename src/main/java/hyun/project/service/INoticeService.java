package hyun.project.service;

import hyun.project.dto.NoticeDTO;
import java.util.List;


public interface INoticeService {


    List<NoticeDTO> getNoticeList();

    NoticeDTO getNoticeInfo(NoticeDTO pDTO, boolean type) throws Exception; // 공지사항 상세 가져오기 위한 정보  type : 조회수 증가 여부


    void updateNoticeInfo(NoticeDTO pDTO) throws Exception;     // 업데이트

    void deleteNoticeInfo(NoticeDTO pDTO) throws Exception;     // 제거

    void insertNoticeInfo(NoticeDTO pDTO) throws Exception;     // 저장




}
