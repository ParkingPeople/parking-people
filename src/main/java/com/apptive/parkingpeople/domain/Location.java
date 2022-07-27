package com.apptive.parkingpeople.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.locationtech.jts.geom.Point;


@Entity
public class Location {

    @Id @GeneratedValue
    @Column(name = "location_id")
    private Long id;

    private LocationType type;

    private String name;

    private String address;

    private Point coordinates;

    private String address_old;

}
