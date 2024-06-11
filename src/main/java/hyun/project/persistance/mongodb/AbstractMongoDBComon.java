package hyun.project.persistance.mongodb;

import com.mongodb.client.model.Indexes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;

@Slf4j
@RequiredArgsConstructor
public abstract class AbstractMongoDBComon {

    /**
     * 컬렉션 생성
     */
    protected boolean createCollection(MongoTemplate mongodb, String colNm) {

        boolean res;

        if (mongodb.collectionExists(colNm)) {
            res = false;
        } else {
            mongodb.createCollection(colNm);
            res = true;
        }
    return res;
    }

    protected boolean createCollection(MongoTemplate mongodb, String colNm, String[] index) {
        log.info(this.getClass().getName() +".인덱스 포함 컬렉션 생성 매퍼 시작");

        boolean res = false;

        if (!mongodb.collectionExists(colNm)) {

            if (index.length > 0) {
                mongodb.createCollection(colNm).createIndex(Indexes.ascending(index));
            } else {
                mongodb.createCollection(colNm);
            }
            res = true;
        }

        log.info(this.getClass().getName() +".인덱스 포함 컬렉션 생성 매퍼 종료");

        return res;
    }

    protected boolean createCollection(MongoTemplate mongodb, String colNm, String index) {
        String[] indexArr = {index};

        return createCollection(mongodb, colNm, indexArr);
    }

}
