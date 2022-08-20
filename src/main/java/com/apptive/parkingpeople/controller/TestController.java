package com.apptive.parkingpeople.controller;

import java.time.LocalDateTime;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.apptive.parkingpeople.domain.ActivityLevel;
import com.apptive.parkingpeople.domain.ParkingLot;
import com.apptive.parkingpeople.domain.PhotoSubmission;
import com.apptive.parkingpeople.repository.ParkingLotRepository;
import com.apptive.parkingpeople.repository.PhotoSubmissionRepository;
import com.apptive.parkingpeople.service.TrafficCongestionService;
import com.fasterxml.jackson.core.JsonProcessingException;

@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestController {

    private final TrafficCongestionService trafficCongestionService;


    // http://localhost:8080/test/best_point?lon=129.086301&lat=35.220105&range=10
    @GetMapping("best_point")
    public String test(@RequestParam("lat") double x, @RequestParam("lon") double y, @RequestParam("range") int range) throws ParseException, JsonProcessingException {
        Point bestPoint = trafficCongestionService.getBestPointByComparing(x, y, range);
        return String.format("nw, ne, sw, se 중에서 가장 교통밀집도가 낮은 지역 : lon = %f, lat = %f", bestPoint.getCoordinate().x, bestPoint.getCoordinate().y);
    }

    // 지울거라서 일단 여기에 다 해놓음.
    private final ParkingLotRepository parkingLotRepository;

    private final PhotoSubmissionRepository photoSubmissionRepository;

    @GetMapping("lot")
    public String parkingLot(
            @RequestParam("lat") double lat,
            @RequestParam("lon") double lon,
            @RequestParam("name") String name,
            @RequestParam("address") String address,
            @RequestParam("base_fee") Long fee,
            @RequestParam("contact") String contact
    ) throws ParseException {

        Optional<ParkingLot> is_used = parkingLotRepository.findByName(name);
        ParkingLot p = new ParkingLot();

        if(!is_used.isEmpty())
            p = is_used.get();

        // Location
        String pointWKT = String.format("POINT(%s %s)", lon, lat);
        // WKTReader를 통한 WKT를 실제 타입으로 변환
        Point point = (Point) new WKTReader().read(pointWKT);
        p.setCoordinates(point);
        p.setName(name);

        p.setAddress(address);
        p.setBase_fee(fee);
        p.setContact(contact);

        System.out.println("save : " + p.getName());
        parkingLotRepository.save(p);

        if(!is_used.isEmpty())
            return "주차장 업데이트";

        return "주차장 등록 완료";
    }

    @GetMapping("photo")
    public String photoSubmission(
            @RequestParam("name") String name,
            @RequestParam("level") int level
    ){
        Optional<ParkingLot> is_used = parkingLotRepository.findByName(name);
        if(is_used.isEmpty()){
            return "해당 주차장이 없습니다.";
        }
        ParkingLot p = is_used.get();

        // PhotoSubmission
        PhotoSubmission photoSubmission = new PhotoSubmission();

        if(level == 0)
            photoSubmission.setPhotoResult(ActivityLevel.FREE);
        else if(level == 1)
            photoSubmission.setPhotoResult(ActivityLevel.NORMAL);
        else if(level == 2)
            photoSubmission.setPhotoResult(ActivityLevel.CROWDED);

        photoSubmission.setLot(p);
        photoSubmission.setTaken_at(LocalDateTime.now());

        photoSubmissionRepository.save(photoSubmission);

        return "주차장 상태(photoResult) 등록";

    }

}
