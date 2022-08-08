package com.apptive.parkingpeople.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.apptive.parkingpeople.domain.ParkingLot;

@Repository
public interface ParkingLotRepository extends JpaRepository<com.apptive.parkingpeople.domain.ParkingLot, Long> {
    ParkingLot findByName(String name);
}
