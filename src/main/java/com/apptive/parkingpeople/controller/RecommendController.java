package com.apptive.parkingpeople.controller;

import com.apptive.parkingpeople.domain.Location;
import com.apptive.parkingpeople.domain.ParkingLot;
import com.apptive.parkingpeople.service.LocationService;
import com.apptive.parkingpeople.service.ParkingLotService;
import com.apptive.parkingpeople.service.TrafficCongestionService;
import com.apptive.parkingpeople.service.WalkingTimeService;
import com.fasterxml.jackson.core.JsonProcessingException;

import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.io.ParseException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/recommend")
@RequiredArgsConstructor
public class RecommendController {

    private final TrafficCongestionService trafficCongestionService;

    private final ParkingLotService parkingLotService;

    private final LocationService locationService;

    private final WalkingTimeService walkingTimeService;

    // http://localhost:8080/recommend?lon=129.086301&lat=35.220105&range=1000
    @GetMapping("")
    public String recommendParkingLots(@RequestParam("lat") double y, @RequestParam("lon") double x,
            @RequestParam("range") double range_km) throws ParseException, JsonProcessingException {

        // 1. 베스트 포인트 확인
        Point bestPoint = trafficCongestionService.getBestPointByComparing(y, x, range_km); // lat, lon, range 순순

        // 2. 베스트 포인트를 이용하여, 구획 내의 주차장 확인
        List<Location> locationsWithinRange = locationService.getLocationsWithinPoint(bestPoint.getCoordinate().y,
                bestPoint.getCoordinate().x, range_km);

        System.out.println("범위 안에 있는 주차장의 개수 : " + locationsWithinRange.size());

        // 3. 구획 내의 주차장 state 업데이트 (24시간 안의 사진들로 state 계산)
        List<ParkingLot> parkingLots = locationsWithinRange
                .stream()
                .filter((o) -> o instanceof ParkingLot)
                .map(o -> (ParkingLot) o)
                .collect(Collectors.toList());

        parkingLotService.updateParkingLotsStateByParkingLots(parkingLots);

        // 4. 각각의 Location(ParkingLot)에 대하여, 보행거리 값 계산.
        // 목적지가 다르고, 그러면 걸리는 시간이 유저마다 다 다르므로, db에 저장하지 않고 메모리에 저장하기.
        Map<Location, Long> locationAndWalkingTime = walkingTimeService.setWalkingTime(locationsWithinRange, x, y);


        // 5. '여유, 보통, 혼잡'순으로 정렬된 것에서 '보행시간'이 짧은것 부터 정렬



        // tmp값 return
        return String.format("범위 안에 있는 주차장의 개수 : %d", locationsWithinRange.size());
    }
}
