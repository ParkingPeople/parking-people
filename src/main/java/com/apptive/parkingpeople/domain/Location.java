package com.apptive.parkingpeople.domain;

import javax.persistence.*;

import lombok.Getter;
import lombok.Setter;
import org.locationtech.jts.geom.Point;


@Entity
@Getter
@Setter
public class Location {

    @Id @GeneratedValue
    @Column(name = "location_id")
    private Long id;

    private String name;

    private String address;

    private Point coordinates;

    private String address_old;

    // 양방향
    @OneToOne(mappedBy = "location")
    private ParkingLot parkingLot;
//
//    @OneToOne(mappedBy = "location")
//    private TrafficData trafficData;

}
