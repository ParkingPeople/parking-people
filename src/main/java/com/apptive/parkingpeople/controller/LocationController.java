package com.apptive.parkingpeople.controller;

import com.apptive.parkingpeople.domain.ParkingLot;
import com.apptive.parkingpeople.service.TrafficDataService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/location")
public class LocationController {

    @Autowired
    TrafficDataService trafficDataService;

    // 밀집도 평균
    @GetMapping("test")
    public String searchWithLocation(@RequestParam("x") float x, @RequestParam("y") float y) throws JsonProcessingException {

        ParkingLot parkingLot = new ParkingLot();
        float avg = trafficDataService.getCongestion(x, y);
        String s = String.format("밀집도 평균 : %f", avg);
        return s;
    }
}
