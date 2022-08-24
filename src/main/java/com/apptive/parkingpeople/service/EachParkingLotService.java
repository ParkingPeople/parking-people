package com.apptive.parkingpeople.service;

import com.apptive.parkingpeople.domain.ParkingLot;
import com.apptive.parkingpeople.dto.EachParkingLotDto;
import com.apptive.parkingpeople.dto.StatusEnum;
import com.apptive.parkingpeople.repository.ParkingLotRepository;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.io.ParseException;
import org.springframework.http.*;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EachParkingLotService {

    private final ParkingLotRepository parkingLotRepository;

    public ResponseEntity<EachParkingLotDto> getEachParkingLot(Long location_id) throws ParseException {

        Optional<ParkingLot> parkingLot = parkingLotRepository.findById(location_id);
        System.out.println("Tesst : " + parkingLot.get().getCoordinates().getX() + parkingLot.get().getCoordinates().getY());

        EachParkingLotDto eachParkingLotDto = new EachParkingLotDto();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));

        eachParkingLotDto.setParkingLot(parkingLot.get());
        eachParkingLotDto.setStatus(StatusEnum.OK);

        return new ResponseEntity<>(eachParkingLotDto, headers, HttpStatus.OK);
    }
}
