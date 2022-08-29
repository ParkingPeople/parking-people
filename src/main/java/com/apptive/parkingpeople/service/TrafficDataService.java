package com.apptive.parkingpeople.service;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;

@Service
public class TrafficDataService {

    @Value("${TMAP_KEY}")
    private String tmapKey;

    public double getCongestion(double x, double y) {

        String result;

        try{
            URL url = new URL("https://apis.openapi.sk.com/tmap/traffic?version=1&" +
                    "centerLat="
                    + x
                    + "&centerLon="
                    + y
                    + "&trafficType=AUTO&zoomLevel=19&appKey="
                    + tmapKey);

            BufferedReader bf;

            bf = new BufferedReader(new InputStreamReader(url.openStream(), StandardCharsets.UTF_8));

            result = bf.readLine();

            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(result);

            JSONArray jsonArray = (JSONArray) jsonObject.get("features");

            int count = jsonArray.size();
            float total = 0;
            float avg;

            for (Object o : jsonArray) {
                JSONObject tmp = (JSONObject) o;
                JSONObject properties = (JSONObject) tmp.get("properties");

                Object congestionBefore = properties.get("congestion");
                if (congestionBefore != null) {
                    total += (Long) properties.get("congestion");
                }
            }

            avg = total / count ;
            return avg;
        }catch (Exception e){
            e.printStackTrace();
            return 2.0; // FIXME : 문제가 있을때 일단은 평균값(2.5)로 주기. 그래야지 전체 결과에 영향이 제일 적음
        }
    }
}
