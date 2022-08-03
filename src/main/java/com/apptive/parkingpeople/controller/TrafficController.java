package com.apptive.parkingpeople.controller;

import com.apptive.parkingpeople.domain.ParkingLot;
import com.apptive.parkingpeople.service.TrafficDataService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/location")
public class TrafficController {

    @Autowired
    TrafficDataService trafficDataService;

    // 밀집도 평균
    // http://localhost:8080/location/test?x=35.229481&y=129.089248 // 예시
    @GetMapping("test")
    public String searchWithLocation(@RequestParam("x") double x, @RequestParam("y") double y) throws JsonProcessingException {

        ParkingLot parkingLot = new ParkingLot();
        double avg = trafficDataService.getCongestion(x, y);
        String s = String.format("밀집도 평균 : %f", avg);
        return s;
    }
}
