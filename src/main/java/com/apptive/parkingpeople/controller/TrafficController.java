package com.apptive.parkingpeople.controller;

import com.apptive.parkingpeople.service.TrafficDataService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/location")
@RequiredArgsConstructor
public class TrafficController {

    private final TrafficDataService trafficDataService;

    // 밀집도 평균
    // http://localhost:8080/location/test?x=35.229481&y=129.089248 // 예시
    @GetMapping("test")
    public String searchWithLocation(@RequestParam("x") double x, @RequestParam("y") double y) throws JsonProcessingException {

        double avg = trafficDataService.getCongestion(x, y);
        return String.format("밀집도 평균 : %f", avg);
    }
}
