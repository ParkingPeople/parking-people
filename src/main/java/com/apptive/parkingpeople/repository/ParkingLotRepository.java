package com.apptive.parkingpeople.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.apptive.parkingpeople.domain.ParkingLot;

import java.util.Optional;

@Repository
public interface ParkingLotRepository extends JpaRepository<com.apptive.parkingpeople.domain.ParkingLot, Long> {
    Optional<ParkingLot> findByName(String name);
}
