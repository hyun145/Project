package hyun.project.service.impl;


import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import hyun.project.dto.FileDTO;
import hyun.project.service.IS3Service;
import hyun.project.util.DateUtil;
import hyun.project.util.FileUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@Slf4j
@RequiredArgsConstructor
@Service
public class S3Service implements IS3Service {

    @Value("${naver.access.key}")
    private String accessKey;


    @Value("${naver.secret.key}")
    private String secretKey;

    // Object Storage 접근 public IP
    final String endPoint = "https://kr.object.ncloudstorage.com";


    final String bucketName = "gradeadvisor";

    private final AmazonS3Client amazonS3Client;


    /**
     * 파일 업로드
     */
    @Override
    public FileDTO uploadFile(MultipartFile mf, String ext) throws Exception {
        log.info(".service 파일 업로드 실행");

        String uploadFilePath = FileUtil.mkdirForData();
        // 웹서버에 저장되는 파일명
        // 업로드하는 파일 이름에 한글, 특수 문자들이 저장될 수 있기 때문에 강제로 영어와 숫자로 구성된 파일명으로 변경 저장
        // 리눅스나 유닉스 등 운영체제는 다국어 지원에 취약하기에
        String uploadFileName = DateUtil.getDateTime("yyMMddHHmmssSSS") + "." + ext;
        String uploadFileUrl = "";

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(mf.getSize());
        objectMetadata.setContentType(mf.getContentType());

        try (InputStream inputStream = mf.getInputStream()) {

            String keyName = uploadFilePath + uploadFileName; // ex) 구분/년/월/일/파일.확장자
            uploadFileUrl = endPoint + "/" + bucketName + keyName;

            log.info("uploadFileName : " + uploadFileName);
            log.info("uploadFilePath : " + uploadFilePath);
            log.info("keyName : " + keyName);

            BasicAWSCredentials awsCredentials = new BasicAWSCredentials(accessKey, secretKey);
            AmazonS3Client amazonS3Client = new AmazonS3Client(awsCredentials);
            amazonS3Client.setEndpoint(endPoint);

            // S3에 폴더 및 파일 업로드
            amazonS3Client.putObject(
                    new PutObjectRequest(bucketName, keyName, inputStream, objectMetadata).
                            withCannedAcl(CannedAccessControlList.PublicRead));  // 공개권한 부여


            // S3에 업로드한 폴더 및 파일 URL
            uploadFileUrl = amazonS3Client.getUrl(bucketName, keyName).toString();

        } catch (IOException e) {
            e.printStackTrace();
            log.error("Filed upload failed", e);

        }

        return FileDTO.builder()
                .saveFileUrl(uploadFileUrl)
                .saveFileName(uploadFileName)
                .build();
    }
}