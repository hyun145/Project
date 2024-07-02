package hyun.project.persistance.mongodb.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import hyun.project.dto.DepartDTO;
import hyun.project.dto.MongoDTO;
import hyun.project.persistance.mongodb.AbstractMongoDBComon;
import hyun.project.persistance.mongodb.IDepartMapper;
import hyun.project.util.CmmUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Component
public class DepartMapper extends AbstractMongoDBComon implements IDepartMapper {

    private final MongoTemplate mongodb;
    @Override
    public int insertData(MongoDTO pDTO, String colNm) throws Exception {

        log.info(this.getClass().getName() +".인설트 매퍼 시작 ");

        int res = 0;

        super.createCollection(mongodb, colNm);

        MongoCollection<Document> col = mongodb.getCollection(colNm);

        col.insertOne(new Document(new ObjectMapper().convertValue(pDTO, Map.class)));

        res = 1;

        log.info(this.getClass().getName() +".인설트 매퍼 종료 ");


        return res;
    }

    @Override
    public int insertDepartInfo(List<DepartDTO> pList, String colNm) throws Exception {
        log.info(this.getClass().getName() +"학과 정보 저장 매퍼 시작");

        int res = 0;

        if(pList == null) {
            pList = new LinkedList<>();
        }
        super.createCollection(mongodb, colNm, "collectTime");
        // 데이터 저장할 컬렉션 생성


        MongoCollection<Document> col = mongodb.getCollection(colNm);
        // 저장할 컬렉션 객체 생성

        for (DepartDTO pDTO : pList) {
            col.insertOne(new Document(new ObjectMapper().convertValue(pDTO, Map.class)));

            res = 1;
        }

        log.info(this.getClass().getName() +"학과 정보 저장 매퍼 종료");
        return res;
    }

    @Override
    public List<DepartDTO> getDepartInfo(String colNm, String query) throws Exception {

        log.info(this.getClass().getName() +".학과 정보 조회 매퍼 시작");
        List<DepartDTO> rList = new LinkedList<>();
        MongoCollection<Document> col = mongodb.getCollection(colNm);

        Document projection = new Document();
        projection.append("_id", 0);

        Document filter = new Document();
        if (query != null && !query.isEmpty()) {
            filter.append("$or", List.of(
                    new Document("korMjrNm", new Document("$regex", query).append("$options", "i")),
                    new Document("korSchlNm", new Document("$regex", query).append("$options", "i")),
                    new Document("lsnTrmNm", new Document("$regex", query).append("$options", "i")),
                    new Document("dghtDivNm", new Document("$regex", query).append("$options", "i")),
                    new Document("korSrsSclftNm", new Document("$regex", query).append("$options", "i"))
            ));     // 검색어가 있을 시 ,
        }
        FindIterable<Document> rs = col.find(filter).projection(projection);


        for (Document doc : rs) {
            String korMjrNm = CmmUtil.nvl(doc.getString("korMjrNm"));
            String korSchlNm = CmmUtil.nvl(doc.getString("korSchlNm"));
            String lsnTrmNm = CmmUtil.nvl(doc.getString("lsnTrmNm"));
            String dghtDivNm = CmmUtil.nvl(doc.getString("dghtDivNm"));
            String korSrsSclftNm = CmmUtil.nvl(doc.getString("korSrsSclftNm"));

            DepartDTO rDTO = DepartDTO.builder().korMjrNm(korMjrNm)
                    .korSchlNm(korSchlNm)
                    .lsnTrmNm(lsnTrmNm)
                    .dghtDivNm(dghtDivNm)
                    .korSrsSclftNm(korSrsSclftNm).build();

            rList.add(rDTO);

        }

        log.info(this.getClass().getName() +".학과 정보 조회 매퍼 종료");
        return rList;
    }
}
