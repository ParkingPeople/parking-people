package com.apptive.parkingpeople.controller;

import java.time.LocalDateTime;

import org.locationtech.jts.geom.Point;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.apptive.parkingpeople.domain.ActivityLevel;
import com.apptive.parkingpeople.domain.ParkingLot;
import com.apptive.parkingpeople.domain.PhotoResult;
import com.apptive.parkingpeople.domain.PhotoSubmission;
import com.apptive.parkingpeople.repository.ParkingLotRepository;
import com.apptive.parkingpeople.repository.PhotoSubmissionRepository;
import com.apptive.parkingpeople.service.ParkingLotService;
import com.apptive.parkingpeople.service.TrafficCongestionService;
import com.fasterxml.jackson.core.JsonProcessingException;

@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    TrafficCongestionService trafficCongestionService;


    // http://localhost:8080/test/best_point?lon=129.086301&lat=35.220105&range=10
    @GetMapping("best_point")
    public String test(@RequestParam("lat") double x, @RequestParam("lon") double y, @RequestParam("range") int range) throws ParseException, JsonProcessingException {
        Point bestPoint = trafficCongestionService.getBestPointByComparing(x, y, range);
        String s = String.format("nw, ne, sw, se 중에서 가장 교통밀집도가 낮은 지역 : lon = %f, lat = %f", bestPoint.getCoordinate().x, bestPoint.getCoordinate().y);
        return s;
    }

    // 지울거라서 일단 여기에 다 해놓음.
    @Autowired
    ParkingLotRepository parkingLotRepository;
    @Autowired
    PhotoSubmissionRepository photoSubmissionRepository;
    @Autowired
    ParkingLotService parkingLotService;

    @GetMapping("lot")
    public String parkingLot(@RequestParam("lat") double lat, @RequestParam("lon") double lon, @RequestParam("name") String name) throws ParseException {
        ParkingLot p = new ParkingLot();

        // Location
        String pointWKT = String.format("POINT(%s %s)", lon, lat);
        // WKTReader를 통한 WKT를 실제 타입으로 변환
        Point point = (Point) new WKTReader().read(pointWKT);
        p.setCoordinates(point);
        p.setName(name);

        parkingLotRepository.save(p);


        return "success";
    }

    @GetMapping("photo")
    public String photoSubmission(@RequestParam("name") String name, @RequestParam("state") int state){
        ParkingLot p = parkingLotRepository.findByName(name);


        // PhotoSubmission
        PhotoSubmission photoSubmission = new PhotoSubmission();
        PhotoResult photoResult = new PhotoResult();
        photoResult.setSubmission(photoSubmission);
        if(state == 0) {
            photoResult.setActivityLevel(ActivityLevel.FREE);
        }
        else if (state == 1) {
            photoResult.setActivityLevel(ActivityLevel.NORMAL);
        }
        else {
            photoResult.setActivityLevel(ActivityLevel.CROWDED);
        }
        System.out.println("here0 : " + p.getName());
        photoSubmission.setLot(p);
        photoSubmission.setTaken_at(LocalDateTime.now());
        System.out.println("here1 : " + photoResult.getActivityLevel());
        System.out.println("here2 : " + photoSubmission.getLot().getName());
        photoSubmissionRepository.save(photoSubmission);
        // TODO: implement this
        // photoResultRepository.save(photoResult);

        return "success";
    }

    @GetMapping("compare")
    public void compare(@RequestParam("name") String name){
        ParkingLot p = parkingLotRepository.findByName(name);
        parkingLotService.setParkingLotState(p);
        return;
    }
}
