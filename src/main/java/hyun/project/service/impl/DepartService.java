package hyun.project.service.impl;

import hyun.project.dto.DepartDTO;
import hyun.project.persistance.mongodb.IDepartMapper;
import hyun.project.service.IDepartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;


@Slf4j
@Service
@RequiredArgsConstructor
public class DepartService implements IDepartService {

    private final IDepartMapper departMapper;
    String apiKey = "TiNl2kb7tOU27HxP2FoNSMFw1yOcD1zfl%2BqVdlHS%2FCrbUPQkyyneVlfZSpZ%2F4MZIkGSv59DggHMTQi7EsIr1gg%3D%3D";


    @Override
    public List<DepartDTO> getDepartInfo() throws Exception {
        StringBuilder urlBuilder = new StringBuilder("http://openapi.academyinfo.go.kr/openapi/service/rest/BasicInformationService/getUniversityMajorCode"); /*URL*/
        urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + "=" + apiKey); /*Service Key*/
        urlBuilder.append("&" + URLEncoder.encode("pageNo", "UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /*페이지번호*/
        urlBuilder.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "=" + URLEncoder.encode("50", "UTF-8")); /*한 페이지 결과 수*/
        urlBuilder.append("&" + URLEncoder.encode("svyYr", "UTF-8") + "=" + URLEncoder.encode("2023", "UTF-8")); /*조사년도*/
        urlBuilder.append("&" + URLEncoder.encode("schlId", "UTF-8") + "=" + URLEncoder.encode("0000200", "UTF-8"));    /* 학교 ID*/
        urlBuilder.append("&" + URLEncoder.encode("schlId", "UTF-8") + "=" + URLEncoder.encode("0000003", "UTF-8"));    /* 학교 ID*/

        List<DepartDTO> pList = new ArrayList<>();
        DepartDTO pDTO;

        URL url = new URL(urlBuilder.toString());

        log.info(String.valueOf(url));
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");
        System.out.println("Response code: " + conn.getResponseCode());
        BufferedReader rd;
        if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } else {
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }
        rd.close();
        conn.disconnect();
        log.info(sb.toString());

        // XML to JSON conversion
        String xml = sb.toString();
        JSONObject json = XML.toJSONObject(xml);
        log.info(json.toString(4)); // 가독성을 위해 들여쓰기

        JSONObject responseBody = json.getJSONObject("response").getJSONObject("body");
        Object items = responseBody.getJSONObject("items").get("item");

        if (items instanceof JSONArray) {

            JSONArray itemArray = (JSONArray) items;
            for (int i = 0; i < itemArray.length(); i++) {
                JSONObject item = itemArray.getJSONObject(i);
                String korMjrNm = item.optString("korMjrNm", "defaultKorMjrNm");  //  학과명
                String korSchlNm = item.optString("korSchlNm", "defaultKorSchlNm");   // 학교명
                String lsnTrmNm = item.optString("lsnTrmNm", "defaultLsnTrmNm");  // 일반 / 전문 구분
                String dghtDivNm = item.optString("dghtDivNm", "defaultDghtDivNm");   // 주야 구분
                String korSrsSclftNm = item.optString("korSrsSclftNm", "defaultKorSrsSclftNm");   // 표준 분류
                pList.add(DepartDTO.builder().korMjrNm(korMjrNm).korSchlNm(korSchlNm)
                        .lsnTrmNm(lsnTrmNm).dghtDivNm(dghtDivNm)
                        .korSrsSclftNm(korSrsSclftNm).build());
            }
        } else if (items instanceof JSONObject) {

            JSONObject itemObject = (JSONObject) items;

            log.info("일로 드렁옴 ㅇㅇ ");

            String korMjrNm = itemObject.optString("korMjrNm", "defaultKorMjrNm");  //  학과명
            String korSchlNm = itemObject.optString("korSchlNm", "defaultKorSchlNm");   // 학교명
            String lsnTrmNm = itemObject.optString("lsnTrmNm", "defaultLsnTrmNm");  // 일반 / 전문 구분
            String dghtDivNm = itemObject.optString("dghtDivNm", "defaultDghtDivNm");   // 주야 구분
            String korSrsSclftNm = itemObject.optString("korSrsSclftNm", "defaultKorSrsSclftNm");   // 표준 분류
            // 필요한 다른 필드들도 추가로 가져올 수 있습니다.

            pList.add(DepartDTO.builder().korMjrNm(korMjrNm).korSchlNm(korSchlNm)
                    .lsnTrmNm(lsnTrmNm).dghtDivNm(dghtDivNm)
                    .korSrsSclftNm(korSrsSclftNm).build());
        }
        return pList;


    }

    /**
     *  1 ~ 1000 까지의 ID를 순차적으로 조회. 학교의 ID를 찾고 리스트에 저장한다.
     */
    @Override
    public List<String> getSchoolId() throws Exception {
        log.info("학교 아이디 찾기 시작");
        List<String> schoolIds = new ArrayList<>();
        for (int i= 2001; i<= 3000; i ++) {
            String schoolId = String.format("%07d", i);
            StringBuilder urlBuilder = new StringBuilder("http://openapi.academyinfo.go.kr/openapi/service/rest/BasicInformationService/getUniversityMajorCode"); /*URL*/
            urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + "=" + apiKey); /*Service Key*/
            urlBuilder.append("&" + URLEncoder.encode("pageNo", "UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /*페이지번호*/
            urlBuilder.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "=" + URLEncoder.encode("50", "UTF-8")); /*한 페이지 결과 수*/
            urlBuilder.append("&" + URLEncoder.encode("svyYr", "UTF-8") + "=" + URLEncoder.encode("2023", "UTF-8")); /*조사년도*/
            urlBuilder.append("&" + URLEncoder.encode("schlId", "UTF-8") + "=" + URLEncoder.encode(schoolId, "UTF-8"));    /* 학교 ID*/

            // 학교 ID에 맞는 데이터를 찾고,

            URL url = new URL(urlBuilder.toString());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-type", "application/json");
            BufferedReader rd;
            if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
                rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            } else {
                rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            }
            StringBuilder sb = new StringBuilder();
            String line;
            while((line = rd.readLine()) != null) {
                sb.append(line);
            }
            rd.close();
            conn.disconnect();

            String xml = sb.toString();
            JSONObject json = XML.toJSONObject(xml);

            JSONObject responseBody = json.getJSONObject("response").getJSONObject("body");
            if (responseBody.getInt("totalCount") >0 ) {
                schoolIds.add(schoolId);

            }
        }
        log.info("학교 아이디 찾기 종료");
        return schoolIds;
    }

    @Override
    public List<DepartDTO> getDepartmentsBySchoolId(String schId) throws Exception {

        log.info("학과 정보 전달 서비스 시작");

        List<DepartDTO> pList = new ArrayList<>();
        StringBuilder urlBuilder = new StringBuilder("http://openapi.academyinfo.go.kr/openapi/service/rest/BasicInformationService/getUniversityMajorCode"); /*URL*/
        urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + "=" + apiKey); /*Service Key*/
        urlBuilder.append("&" + URLEncoder.encode("pageNo", "UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /*페이지번호*/
        urlBuilder.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "=" + URLEncoder.encode("30", "UTF-8")); /*한 페이지 결과 수*/
        urlBuilder.append("&" + URLEncoder.encode("svyYr", "UTF-8") + "=" + URLEncoder.encode("2023", "UTF-8")); /*조사년도*/
        urlBuilder.append("&" + URLEncoder.encode("schlId", "UTF-8") + "=" + URLEncoder.encode(schId, "UTF-8"));    /* 학교 ID*/

        URL url = new URL(urlBuilder.toString());

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");
        BufferedReader rd;
        if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } else {
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }

        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }
        rd.close();
        conn.disconnect();


        String xml = sb.toString();
        JSONObject json = XML.toJSONObject(xml);

        JSONObject responseBody = json.getJSONObject("response").getJSONObject("body");
        Object items = responseBody.getJSONObject("items").get("item");

        if (items instanceof JSONArray) {
            JSONArray itemArray = (JSONArray) items;
            for (int i = 0; i < itemArray.length(); i++) {
                JSONObject item = itemArray.getJSONObject(i);
                pList.add(parseDepartment(item));
            }
        } else if (items instanceof JSONObject) {
            JSONObject itemObject = (JSONObject) items;
            pList.add(parseDepartment(itemObject));
        }
        log.info("학과 정보 전달 서비스 종료");
        return pList;

    }

    /**
     * 값 저장
     */
    private DepartDTO parseDepartment(JSONObject item) {
        String korMjrNm = item.optString("korMjrNm", "defaultKorMjrNm");  // 학과명
        String korSchlNm = item.optString("korSchlNm", "defaultKorSchlNm");   // 학교명
        String lsnTrmNm = item.optString("lsnTrmNm", "defaultLsnTrmNm");  // 일반 / 전문 구분
        String dghtDivNm = item.optString("dghtDivNm", "defaultDghtDivNm");   // 주야 구분
        String korSrsSclftNm = item.optString("korSrsSclftNm", "defaultKorSrsSclftNm");   // 표준 분류

        return DepartDTO.builder()
                .korMjrNm(korMjrNm)
                .korSchlNm(korSchlNm)
                .lsnTrmNm(lsnTrmNm)
                .dghtDivNm(dghtDivNm)
                .korSrsSclftNm(korSrsSclftNm)
                .build();
    }

    @Override
    public List<DepartDTO> getDepartInfoTest() throws Exception {

        log.info("학과 정보 저장 서비스 시작");
        List<String> schIds = getSchoolId();
        log.info("학교 코드 : " + schIds);
        List<DepartDTO> rList = new ArrayList<>();

        for (String schId : schIds) {
            List<DepartDTO> departs = getDepartmentsBySchoolId(schId);

            log.info("departs : " + departs);
            rList.addAll(departs);
        }

        log.info("학과 정보 저장 서비스 종료");
        return rList;
    }

    @Override
    public int collectDepartInfo() throws Exception {

        log.info("몽고 DB 학과 정보 저장 서비스 시작!");
        int res = 0;

        String colNm = "DepartInfo";

        List<DepartDTO> rList = this.getDepartInfoTest();   // 결과 받고,

        res = departMapper.insertDepartInfo(rList, colNm);


        log.info("몽고 DB 학과 정보 저장 서비스 종료!");

        return res;
    }

    @Override
    public Page<DepartDTO> getDepartInfoList(Pageable page, String query) throws Exception {
        log.info(this.getClass().getName() +"학과 정보 조회 서비스 시작 ");

        String colNm = "DepartInfo";
        List<DepartDTO> rList = departMapper.getDepartInfo(colNm, query);

        int start = (int) page.getOffset();
        int end = Math.min((start + page.getPageSize()), rList.size());
        Page<DepartDTO> rListPage = new PageImpl<>(rList.subList(start, end), page, rList.size());



        log.info(this.getClass().getName() +"학과 정보 조회 서비스 종료 ");
        return rListPage;

    }
}


