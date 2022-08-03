package com.apptive.parkingpeople.domain;

import javax.persistence.*;

import lombok.Getter;
import lombok.Setter;
import org.locationtech.jts.geom.Point;

//TODO Point 사용시 Loation이라는 라이브러리 있어서 일단은 LocationDomain으로 바꿔놨음.
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
//    @OneToOne(mappedBy = "location")
//    private ParkingLot parkingLot;
//
//    @OneToOne(mappedBy = "location")
//    private TrafficData trafficData;

}
