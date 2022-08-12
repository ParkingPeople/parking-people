package com.apptive.parkingpeople.controller;

import com.apptive.parkingpeople.domain.Location;
import com.apptive.parkingpeople.repository.LocationRepository;
import com.apptive.parkingpeople.repository.ParkingLotRepository;
import com.apptive.parkingpeople.service.WalkingTimeService;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
class getWalkingTimeTest {

    @Autowired
    LocationRepository locationRepository;

    @Autowired
    WalkingTimeService walkingTimeService;

    @Autowired
    ParkingLotRepository parkingLotRepository;

    @Test
    @Rollback(false)
    String getAllWalkingTime() throws ParseException {
        //Given

        String pointWKT = String.format("POINT(%s %s)", 129.088126, 35.237909);
        Point point = (Point) new WKTReader().read(pointWKT);
        Location location1 = new Location();
        location1.setCoordinates(point);



        pointWKT = String.format("POINT(%s %s)", 129.089399, 35.229589);
        point = (Point) new WKTReader().read(pointWKT);
        Location location2 = new Location();
        location1.setCoordinates(point);


        pointWKT = String.format("POINT(%s %s)", 129.086301, 35.220105);
        point = (Point) new WKTReader().read(pointWKT);
        Location location3 = new Location();
        location1.setCoordinates(point);


        pointWKT = String.format("POINT(%s %s)", 35.212670, 35.212670);
        point = (Point) new WKTReader().read(pointWKT);
        Location location4 = new Location();
        location1.setCoordinates(point);

        //When
        List<Location> locations = new ArrayList<>();
        locations.add(location1);
        locations.add(location2);
        locations.add(location3);
        locations.add(location4);

        //Then
        walkingTimeService.setWalkingTime(locations, 129.086301, 35.220105);

        return "success";

    }
}