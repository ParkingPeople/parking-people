package com.apptive.parkingpeople.service;

import com.apptive.parkingpeople.domain.Location;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class WalkingTimeService {

    @Value("${tmap-key}")
    private String tmapKey;

    public Map<Location,Long> setWalkingTime(List<Location> locations, double endX, double endY){
        Map<Location, Long> walkingTimeMap = new HashMap<>();

        for (Location location : locations) {
            Long time = getEachWalkingTime(location, endX, endY);
            walkingTimeMap.put(location, time);

            // FIXME 일정 시간 동안 요청 개수 제한이 있는거 같아서, 지연으로 보내는 방법 사용. 제일 짧은 간격이 0.25초
            try {
                TimeUnit.MILLISECONDS.sleep(250);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }

        return walkingTimeMap;
    }

    public Long getEachWalkingTime(Location location, double endX, double endY){

        String result;

        try{
            URL url = new URL("https://apis.openapi.sk.com/tmap/routes/pedestrian?" +
                    "startX="
                    + location.getCoordinates().getX()
                    + "&startY="
                    + location.getCoordinates().getY()
                    + "&endX="
                    + endX
                    + "&endY="
                    + endY
                    + "&startName=%EC%B6%9C%EB%B0%9C&endName=%EB%B3%B8%EC%82%AC&appKey="
                    + tmapKey);

            BufferedReader bf;

            bf = new BufferedReader(new InputStreamReader(url.openStream(), StandardCharsets.UTF_8));

            result = bf.readLine();

            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(result);

            JSONArray jsonArray = (JSONArray) jsonObject.get("features");

            Long walkingTime;

            JSONObject tmp = (JSONObject) jsonArray.get(0);
            JSONObject properties = (JSONObject) tmp.get("properties");
            walkingTime = (Long) properties.get("totalTime");

            System.out.println("장소 : " + location.getName() + ", 걸리는 시간(초) : " + walkingTime);
            return walkingTime;

        }catch(Exception e){
            System.out.println("확인 불가 장소 : " + location.getName());
            e.printStackTrace();
            return 0L; // FIXME '0'을 확인 불가 장소로 인식하게 할지, 아니면 다른 방법이 있는지 생각하기
        }
    }



}
