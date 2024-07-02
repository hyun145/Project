package hyun.project.service;

import hyun.project.dto.DepartDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IDepartService {

    public List<DepartDTO> getDepartInfo() throws Exception;

    public List<String> getSchoolId() throws Exception;

    public List<DepartDTO> getDepartmentsBySchoolId(String schId) throws Exception;

    public List<DepartDTO> getDepartInfoTest() throws Exception;

    int collectDepartInfo() throws Exception;

    Page<DepartDTO> getDepartInfoList(Pageable page, String query) throws Exception;


}
