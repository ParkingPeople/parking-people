package com.apptive.parkingpeople.repository;

import com.apptive.parkingpeople.domain.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParkingLotLocationRepository extends JpaRepository<Location, Long> {

}
