package com.apptive.parkingpeople.controller;

import com.apptive.parkingpeople.domain.Location;
import com.apptive.parkingpeople.domain.ParkingLot;
import com.apptive.parkingpeople.dto.ParkingLotDto;
import com.apptive.parkingpeople.service.LocationService;
import com.apptive.parkingpeople.service.MLService;
import com.apptive.parkingpeople.service.ParkingLotService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class MLController {

    private final MLService mlService;

    private final LocationService locationService;

    private final ParkingLotService parkingLotService;

    @GetMapping("/parking-lots")
    public ResponseEntity<ParkingLotDto> findParkingLotsByPoint(@RequestParam("lat") double lat, @RequestParam("lon") double lon){
        List<Location> locationsWithinPoint = locationService.getLocationsWithinPoint(lat, lon, 0.200);// 100m
        List<ParkingLot> parkingLotsByLocations = parkingLotService.findParkingLotsByLocations(locationsWithinPoint);

        ParkingLotDto parkingLotDto = new ParkingLotDto();
        parkingLotDto.setParkingLots(parkingLotsByLocations);
        parkingLotDto.setCount((long) parkingLotsByLocations.size());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));

        return new ResponseEntity<>(parkingLotDto, headers, HttpStatus.OK);
    }



    @PostMapping("/upload/file")
    public Boolean uploadFile(@RequestParam("id") Long id, @RequestParam("file") MultipartFile file){

        double emptyProbability = mlService.setActivityLevel(id, file);

        return emptyProbability != 500;
    }
}
