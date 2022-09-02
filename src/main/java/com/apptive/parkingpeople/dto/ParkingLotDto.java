package com.apptive.parkingpeople.dto;

import com.apptive.parkingpeople.domain.ParkingLot;
import lombok.Data;

import java.util.List;

@Data
public class ParkingLotDto {

    public List<ParkingLot> parkingLots;

    private Long count;
}
