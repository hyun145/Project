package hyun.project.service.impl;

import hyun.project.dto.MongoDTO;
import hyun.project.persistance.mongodb.IDepartMapper;
import hyun.project.service.IMongoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class MongoService implements IMongoService {

    private final IDepartMapper mongoMapper;
    @Override
    public int mongoTest(MongoDTO pDTO) throws Exception {

        log.info(this.getClass().getName() +".데이터 저장 서비스 시작.");

        String colNm = "MONGO_TEST";

        int res = mongoMapper.insertData(pDTO, colNm);


        log.info(this.getClass().getName() +".데이터 저장 서비스 종료.");

        return res;
    }
}
