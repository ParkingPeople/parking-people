package com.apptive.parkingpeople.controller;

import com.apptive.parkingpeople.dto.EachParkingLotDto;
import com.apptive.parkingpeople.service.EachParkingLotService;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.io.ParseException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/each")
@RequiredArgsConstructor
public class EachParkingLotController {

    private final EachParkingLotService eachParkingLotService;

    @GetMapping("/{location_id}")
    public ResponseEntity<EachParkingLotDto> eachParkingLot(
            @PathVariable("location_id") Long location_id
    ) throws ParseException {
        return eachParkingLotService.getEachParkingLot(location_id); // exception 없애기
    }
}
