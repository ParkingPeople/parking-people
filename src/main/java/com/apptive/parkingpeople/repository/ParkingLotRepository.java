package com.apptive.parkingpeople.repository;

import com.apptive.parkingpeople.domain.ParkingLot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParkingLotRepository extends JpaRepository<com.apptive.parkingpeople.domain.ParkingLot, Long> {
    ParkingLot findByName(String name);
}
