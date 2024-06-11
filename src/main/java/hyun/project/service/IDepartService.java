package hyun.project.service;

import hyun.project.dto.DepartDTO;

import java.util.List;

public interface IDepartService {

    public List<DepartDTO> getDepartInfo() throws Exception;

    public List<String> getSchoolId() throws Exception;

    public List<DepartDTO> getDepartmentsBySchoolId(String schId) throws Exception;

    public List<DepartDTO> getDepartInfoTest() throws Exception;

    int collectDepartInfo() throws Exception;

    List<DepartDTO> getDepartInfoList() throws Exception;

}
