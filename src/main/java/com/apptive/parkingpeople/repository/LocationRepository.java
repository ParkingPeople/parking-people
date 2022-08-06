package com.apptive.parkingpeople.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationRepository extends JpaRepository<com.apptive.parkingpeople.domain.Location, Long> {

}
