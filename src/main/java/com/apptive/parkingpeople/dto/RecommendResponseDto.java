package com.apptive.parkingpeople.dto;

import com.apptive.parkingpeople.domain.ParkingLot;
import lombok.Data;

import java.util.List;

@Data
public class RecommendResponseDto {

    public List<ParkingLot> parkingLots;

    private StatusEnum status;

    private int total_count;




}
