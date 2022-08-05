package com.apptive.parkingpeople.controller;

import com.apptive.parkingpeople.service.TrafficCongestionService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.io.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @Autowired
    TrafficCongestionService trafficCongestionService;


    // http://localhost:8080/test?lon=129.086301&lat=35.220105&range=10
    @GetMapping("/test")
    public String test(@RequestParam("lat") double x, @RequestParam("lon") double y, @RequestParam("range") int range) throws ParseException, JsonProcessingException {
        Point bestPoint = trafficCongestionService.getBestPointByComparing(x, y, range);
        String s = String.format("nw, ne, sw, se 중에서 가장 교통밀집도가 낮은 지역 : lon = %f, lat = %f", bestPoint.getCoordinate().x, bestPoint.getCoordinate().y);
        return s;
    }
}
