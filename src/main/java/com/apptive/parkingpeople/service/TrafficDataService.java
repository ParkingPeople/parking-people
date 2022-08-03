package com.apptive.parkingpeople.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

@Service
public class TrafficDataService {

    @Value("${tmap-key}")
    private String tmapKey;

    public double getCongestion(double x, double y) throws JsonProcessingException {

        String result = "";

        try{
            URL url = new URL("https://apis.openapi.sk.com/tmap/traffic?version=1&" +
                    "centerLat="
                    + x
                    + "&centerLon="
                    + y
                    + "&trafficType=AUTO&zoomLevel=19&appKey="
                    + tmapKey);

            BufferedReader bf;

            bf = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));

            result = bf.readLine();

            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(result);

            JSONArray jsonArray = (JSONArray) jsonObject.get("features");

            int count = jsonArray.size();
            float total = 0;
            float avg = 0;

            for(int i = 0; i < jsonArray.size(); i ++){
                JSONObject tmp = (JSONObject) jsonArray.get(i);
                JSONObject properties = (JSONObject) tmp.get("properties");

                Object congestionBefore = properties.get("congestion");
                if(congestionBefore != null){
                    total += (Long)properties.get("congestion");
                }
            }

            avg = total / count ;
            return avg;
        }catch (Exception e){
            e.printStackTrace();
            return 0.0;
        }
    }
}
