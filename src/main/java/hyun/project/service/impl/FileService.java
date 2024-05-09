package hyun.project.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import hyun.project.dto.FileDTO;
import hyun.project.repository.FileRepository;
import hyun.project.repository.entity.FileEntity;
import hyun.project.service.IFileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class FileService implements IFileService {

    private final FileRepository fileRepository;
    /**
     * 파일 저장

     */
    @Override
    public void saveFiles(FileDTO pDTO) throws Exception {
        log.info(this.getClass().getName() +".saveFiles Start!");

        fileRepository.save(FileEntity.builder().
                boardSeq(pDTO.boardSeq())
                .orgFileName(pDTO.orgFileName())
                .saveFilePath(pDTO.saveFilePath())
                .fileSize(pDTO.fileSize())
                .saveFileName(pDTO.saveFileName())
                .saveFileUrl(pDTO.saveFileUrl()).build());

        log.info(this.getClass().getName() +".saveFiles End!");
    }

    /**
     * 이미지 삭제
     */
    @Override
    public void deleteFiles(Long boardSeq) throws Exception {
        log.info(this.getClass().getName() +".deleteFiles Start!");

        List<FileEntity> filesDelete = fileRepository.findByBoardSeq(boardSeq)
                .orElse(Collections.emptyList());

        for (FileEntity file : filesDelete) {
            fileRepository.delete(file);
        }


        log.info(this.getClass().getName() +".deleteFiles End!");
    }

    /**
     * 경로 가져오기
     */
    @Override
    public List<FileDTO> getFilePath(Long boardSeq) throws Exception {

        log.info(this.getClass().getName() +".getFilePath Start!");

        List<FileEntity> pEntity = fileRepository.findByBoardSeq(boardSeq).orElse(Collections.emptyList());

        log.info(this.getClass().getName() +".getFilePath End!");

        List<FileDTO> rList = new ObjectMapper().convertValue(pEntity,
                new TypeReference<>() {
                });


        return rList;
    }
}
