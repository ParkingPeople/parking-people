package com.apptive.parkingpeople.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.awt.*;

@Entity
public class Location {

    @Id @GeneratedValue
    @Column(name = "location_id")
    private Long id;

    private Type type;

    private String name;

    private String address;

    private Point coordinates;

    private String address_old;
}
