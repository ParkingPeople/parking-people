package com.apptive.parkingpeople.domain;

import javax.persistence.*;

import org.locationtech.jts.geom.Point;


@Entity
public class Location {

    @Id @GeneratedValue
    @Column(name = "location_id")
    //@OneToOne(mappedBy = "location", fetch = FetchType.LAZY) //TODO @OneToOne일 경우, Long 또는 int를 못 받아서 다른걸로 해야 할거 같아요
    private Long id;

    @Enumerated(EnumType.STRING)
    private LocationType type;

    private String name;

    private String address;

    private Point coordinates;

    private String address_old;

}
