package com.apptive.parkingpeople.controller;

import com.apptive.parkingpeople.domain.Location;
import com.apptive.parkingpeople.domain.ParkingLot;
import com.apptive.parkingpeople.service.LocationService;
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
import java.util.stream.Collectors;

@RestController
@RequestMapping("/recommend")
public class RecommendController {

    @Autowired
    TrafficCongestionService trafficCongestionService;

    @Autowired
    ParkingLotService parkingLotService;

    @Autowired
    LocationService locationService;

    // http://localhost:8080/recommend?lon=129.086301&lat=35.220105&range=1000
    @GetMapping("")
    public String recommendParkingLots(@RequestParam("lat") double y, @RequestParam("lon") double x,
            @RequestParam("range") int range) throws ParseException, JsonProcessingException {
        // 1. 베스트 포인트 확인
        Point bestPoint = trafficCongestionService.getBestPointByComparing(y, x, range); // lat, lon, range 순순
        String s = String.format("nw, ne, sw, se 중에서 가장 교통밀집도가 낮은 지역 : lon = %f, lat = %f", bestPoint.getCoordinate().x,
                bestPoint.getCoordinate().y);

        // 2. 베스트 포인트를 이용하여, 구획 내의 주차장 확인
        List<Location> locationsWithinRange = locationService.getLocationsWithinPoint(bestPoint.getCoordinate().y,
                bestPoint.getCoordinate().x, range / 2);
        // List<Location> locationsWithinRange =
        // parkingLotService.getParkingLotsWithPoint(bestPoint.getCoordinate().y,
        // bestPoint.getCoordinate().x, range / 2);

        // 3. 구획 내의 주차장 state 업데이트 (24시간 안의 사진들로 state 계산)
        System.out.println("범위 안에 있는 주차장의 개수 : " + locationsWithinRange.size());
        parkingLotService.updateParkingLotsStateByParkingLots(
                locationsWithinRange
                        .stream()
                        .filter((o) -> o instanceof ParkingLot)
                        .map(o -> (ParkingLot) o)
                        .collect(Collectors.toList()));

        // 4. 거리와 state를 이용하여 정렬

        return "success";
    }
}
