package com.apptive.parkingpeople.controller;

import com.apptive.parkingpeople.domain.Location;
import com.apptive.parkingpeople.service.LocationService;
import com.apptive.parkingpeople.service.MLService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class MLController {

    private final MLService mlService;

    private final LocationService locationService;

    @PostMapping("/upload/file")
    public ResponseEntity<Void> uploadFile(@RequestParam("lat") double lat, @RequestParam("lon") double lon, @RequestParam("file") MultipartFile file){

        double range_km = 0.050;

        List<Location> locations;

        while(true) {
            locations = locationService.getLocationsWithinPoint(lat, lon, range_km);
            if (!locations.isEmpty())
                break;

            if(range_km > 1) {
                log.warn("위도 경도로 주차장을 찾을 수 없습니다.");
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            range_km += 0.050;
        }

        return mlService.setActivityLevel(locations, file);
    }
}
