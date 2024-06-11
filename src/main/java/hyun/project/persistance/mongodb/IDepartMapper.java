package hyun.project.persistance.mongodb;


import hyun.project.dto.DepartDTO;
import hyun.project.dto.MongoDTO;

import java.util.List;

public interface IDepartMapper {

    int insertData(MongoDTO pDTO, String colNm) throws Exception;

    int insertDepartInfo(List<DepartDTO> pList,String colNm) throws Exception;


    List<DepartDTO> getDepartInfo(String colNm) throws Exception;



}
