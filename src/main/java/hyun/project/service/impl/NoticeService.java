package hyun.project.service.impl;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import hyun.project.dto.NoticeDTO;
import hyun.project.repository.NoticeRepository;
import hyun.project.repository.entity.NoticeEntity;
import hyun.project.service.INoticeService;
import hyun.project.util.CmmUtil;
import hyun.project.util.DateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class NoticeService implements INoticeService {

    // RequiredArgsConstructor 어노테이션으로 생성자를 자동 생성함
    // noticeRepository 변수에 이미 메모리에 올라간 NoticeRepository 객체를 넣어줌
    // 예전에는 autowired 어노테이션를 통해 설정했었지만, 이젠 생성자를 통해 객체 주입함
    private final NoticeRepository noticeRepository;

    @Override
    public List<NoticeDTO> getNoticeList() {

        log.info(this.getClass().getName() + ".getNoticeList Start!");

        // 공지사항 전체 리스트 조회하기
        List<NoticeEntity> rList = noticeRepository.findAllByOrderByNoticeSeqDesc();

        // 엔티티의 값들을 DTO에 맞게 넣어주기
        List<NoticeDTO> nList = new ObjectMapper().convertValue(rList,
                new TypeReference<>() {
                });

        log.info(this.getClass().getName() + ".getNoticeList End!");

        return nList;
    }

    @Transactional
    @Override
    public NoticeDTO getNoticeInfo(NoticeDTO pDTO, boolean type) {

        log.info(this.getClass().getName() + ".getNoticeInfo Start!");

        if (type) {     // 상세 화면만 조회수 증가하고, 수정 화면은 조회수 증가 안함.
            // 조회수 증가하기
            int res = noticeRepository.updateReadCnt(pDTO.noticeSeq());

            // 조회수 증가 성공여부 체크
            log.info("res : " + res);
        }

        // 공지사항 상세내역 가져오기
        NoticeEntity rEntity = noticeRepository.findByNoticeSeq(pDTO.noticeSeq());

        // 엔티티의 값들을 DTO에 맞게 넣어주기
        NoticeDTO rDTO = new ObjectMapper().convertValue(rEntity, NoticeDTO.class);  // DTO 변환하기.

        log.info(this.getClass().getName() + ".getNoticeInfo End!");

        return rDTO;
    }

    @Transactional
    @Override
    public void updateNoticeInfo(NoticeDTO pDTO) {

        log.info(this.getClass().getName() + ".updateNoticeInfo Start!");

        Long noticeSeq = pDTO.noticeSeq();

        String title = CmmUtil.nvl(pDTO.title());
        String contents = CmmUtil.nvl(pDTO.contents());
        String userId = CmmUtil.nvl(pDTO.userId());
        String nickName = CmmUtil.nvl(pDTO.nickName());

        log.info("noticeSeq : " + noticeSeq);
        log.info("title : " + title);
        log.info("contents : " + contents);
        log.info("userId : " + userId);
        log.info("nickName : " + nickName);

        // 현재 공지사항 조회수 가져오기
        NoticeEntity rEntity = noticeRepository.findByNoticeSeq(noticeSeq);

        // 수정할 값들을 빌더를 통해 엔티티에 저장하기
        NoticeEntity pEntity = NoticeEntity.builder()
                .noticeSeq(noticeSeq).title(title).contents(contents).userId(userId)
                .nickName(nickName)
                .readCnt(rEntity.getReadCnt())
                .build();

        // 데이터 수정하기
        noticeRepository.save(pEntity);

        log.info(this.getClass().getName() + ".updateNoticeInfo End!");

    }

    @Override
    public void deleteNoticeInfo(NoticeDTO pDTO)  {

        log.info(this.getClass().getName() + ".deleteNoticeInfo Start!");

        Long noticeSeq = pDTO.noticeSeq();

        log.info("noticeSeq : " + noticeSeq);

        // 데이터 수정하기
        noticeRepository.deleteById(noticeSeq);


        log.info(this.getClass().getName() + ".deleteNoticeInfo End!");
    }

    @Override
    public void insertNoticeInfo(NoticeDTO pDTO) throws Exception {

        log.info(this.getClass().getName() + ".InsertNoticeInfo Start!");

        String title = CmmUtil.nvl(pDTO.title());
        String contents = CmmUtil.nvl(pDTO.contents());
        String nickName = CmmUtil.nvl(pDTO.nickName());
        String userId = CmmUtil.nvl(pDTO.userId());
        log.info("title : " + title);
        log.info("contents : " + contents);
        log.info("nickName : " + nickName);
        log.info("userId : " + userId);

        // 공지사항 저장을 위해서는 PK 값은 빌더에 추가하지 않는다.
        // JPA에 자동 증가 설정을 해놨음
        NoticeEntity pEntity = NoticeEntity.builder()
                .title(title).contents(contents).nickName(nickName).readCnt(0L)
                .regId(nickName).regDt(DateUtil.getDateTime("yyyy-MM-dd hh:mm:ss"))
                .chgId(nickName).chgDt(DateUtil.getDateTime("yyyy-MM-dd hh:mm:ss"))
                .userId(userId).build();

        // 공지사항 저장하기
        noticeRepository.save(pEntity);

        log.info(this.getClass().getName() + ".InsertNoticeInfo End!");

    }

    @Override
    public List<NoticeDTO> boardSearchList(NoticeDTO pDTO) throws Exception {

        log.info(this.getClass().getName() +".boardSearchList Start!");
        String keyWord = CmmUtil.nvl(pDTO.title());


        List<NoticeEntity> rEntity = noticeRepository.findByTitleContaining(keyWord);


        // 엔티티의 값들을 DTO에 맞게 넣어주기
        List<NoticeDTO> nList = new ObjectMapper().convertValue(rEntity,
                new TypeReference<>() {
                });

        log.info(this.getClass().getName() +".boardSearchList End!");
        return nList;
    }


}