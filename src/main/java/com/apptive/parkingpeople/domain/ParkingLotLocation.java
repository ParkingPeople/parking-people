package com.apptive.parkingpeople.domain;

import javax.persistence.*;

import lombok.Getter;
import lombok.Setter;
import org.locationtech.jts.geom.Point;

//TODO Point 사용시 Loation이라는 라이브러리 있어서 일단은 LocationDomain으로 바꿔놨음.
@Entity
@Getter
@Setter
public class ParkingLotLocation {

    @Id @GeneratedValue
    @Column(name = "parking_lot_location_id")
    private Long id;

//    PakringLot에 type이 있는데 중복?
//    @Enumerated(EnumType.STRING)
//    private ?? type;

    private String name;

    private String address;

    private Point coordinates;

    private String address_old;

    // 양방향
//    @OneToOne(mappedBy = "locationDomain")
//    private ParkingLot parkingLot;
//
//    @OneToOne(mappedBy = "locationDomain")
//    private TrafficData trafficData;

}
