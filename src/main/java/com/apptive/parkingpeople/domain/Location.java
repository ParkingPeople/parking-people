package com.apptive.parkingpeople.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import org.locationtech.jts.geom.Point;

import lombok.Data;


@Data
@Entity
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
