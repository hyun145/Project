package hyun.project.service;

import hyun.project.dto.FileDTO;

import java.util.List;

public interface IFileService {

    /**
     * 파일 저장
     */
    void saveFiles(FileDTO pDTO) throws Exception;

    /**
     * 이미지 삭제
     */
    void deleteFiles(Long boardSeq) throws Exception;


    /**
     * 경로 가져오기
     */
    List<FileDTO> getFilePath(final Long boardSeq) throws Exception;

}
