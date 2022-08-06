package com.apptive.parkingpeople.controller;

import com.apptive.parkingpeople.domain.Location;
import com.apptive.parkingpeople.service.ParkingLotService;
import com.apptive.parkingpeople.service.TrafficCongestionService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.io.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/recommend")
public class RecommendController {

    @Autowired
    TrafficCongestionService trafficCongestionService;

    @Autowired
    ParkingLotService parkingLotService;

    // http://localhost:8080/recommend?lon=129.086301&lat=35.220105&range=1000
    @GetMapping("")
    public String recommendParkingLots(@RequestParam("lat") double y, @RequestParam("lon") double x, @RequestParam("range") int range) throws ParseException, JsonProcessingException {
        // 1. 베스트 포인트 확인
        Point bestPoint = trafficCongestionService.getBestPointByComparing(y, x, range); // lat, lon, range 순순
        String s = String.format("nw, ne, sw, se 중에서 가장 교통밀집도가 낮은 지역 : lon = %f, lat = %f", bestPoint.getCoordinate().x, bestPoint.getCoordinate().y);

        // 2. 베스트 포인트를 이용하여, 구획내의 주차장 확인
        List<Location> parkingLotsWithPoint = parkingLotService.getParkingLotsWithPoint(bestPoint.getCoordinate().y, bestPoint.getCoordinate().x, range / 2);


        return "success";
    }
}
