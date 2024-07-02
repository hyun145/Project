package hyun.project.service;

import hyun.project.dto.ScoreDTO;

import java.util.List;
import java.util.Map;

public interface IScoreService {

    public void saveScore(Map<String, Object> payload, String userId) throws Exception;

    public List<ScoreDTO> getScore(String userId) throws Exception;

    public void saveScores(String userId, String subject, int grade, int standardScore) throws Exception;


}
