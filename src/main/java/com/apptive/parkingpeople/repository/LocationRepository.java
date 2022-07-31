package com.apptive.parkingpeople.repository;

import com.apptive.parkingpeople.domain.LocationDomain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationRepository extends JpaRepository<LocationDomain, Long> {

}
