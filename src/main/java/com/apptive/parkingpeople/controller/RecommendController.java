package com.apptive.parkingpeople.controller;

import com.apptive.parkingpeople.domain.Location;
import com.apptive.parkingpeople.domain.ParkingLot;
import com.apptive.parkingpeople.dto.RecommendResponseDto;
import com.apptive.parkingpeople.service.LocationService;
import com.apptive.parkingpeople.service.ParkingLotService;
import com.apptive.parkingpeople.service.TrafficCongestionService;
import com.apptive.parkingpeople.service.WalkingTimeService;
import com.fasterxml.jackson.core.JsonProcessingException;

import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.io.ParseException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class RecommendController {

    private final TrafficCongestionService trafficCongestionService;

    private final ParkingLotService parkingLotService;

    private final LocationService locationService;

    private final WalkingTimeService walkingTimeService;

    // http://localhost:8080/recommend?lon=129.086301&lat=35.220105&range=1000
    @GetMapping("/parking-lots/recommend")
    public ResponseEntity<RecommendResponseDto> recommendParkingLots(@RequestParam("lat") double y, @RequestParam("lon") double x,
                                                                     @RequestParam("range") double range_km) throws ParseException, JsonProcessingException {

//        // 1. 베스트 포인트 확인
//        Point bestPoint = trafficCongestionService.getBestPointByComparing(y, x, range_km); // lat, lon, range 순순
//
//        // 2. 베스트 포인트를 이용하여, 구획 내의 주차장 확인
//        List<Location> locationsWithinRange = locationService.getLocationsWithinPoint(bestPoint.getCoordinate().y,
//                bestPoint.getCoordinate().x, range_km);

        // 원래는 4분할 하고 하나의 영역을 택하는게 맞는데(위의 1번 2번), 개수때문에 일단 아래와 같이 함. // TODO 서버에 띄우고 개수 확인하기
        // 1` + 2`. 거리안의 주차장 찾기
        List<Location> locationsWithinRange = locationService.getLocationsWithinPoint(y,
                x, range_km);

        System.out.println("범위 안에 있는 주차장의 개수 : " + locationsWithinRange.size());

        // 3. 구획 내의 주차장 state 업데이트 (24시간 안의 사진들로 state 계산)
        List<ParkingLot> parkingLots = locationsWithinRange
                .stream()
                .filter((o) -> o instanceof ParkingLot)
                .map(o -> (ParkingLot) o)
                .collect(Collectors.toList());

        parkingLotService.updateParkingLotsStateByParkingLots(parkingLots);

        // 4. 각각의 ParkingLot에 대하여, 목적지까지의 보행시간과 거리 계산.
        walkingTimeService.setWalkingTimeAndDistance(parkingLots, x, y);

        // 5. activityLevel(여유, 보통, 혼잡)과 보행자 경로 시간을 이용하여 주차장 추천 순위 정렬
        return parkingLotService.prioritizeParkingLotUsingActivityLevelAndWalkingTime(parkingLots);
    }
}
