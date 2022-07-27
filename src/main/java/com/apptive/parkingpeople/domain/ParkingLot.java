package com.apptive.parkingpeople.domain;

import javax.persistence.*;

@Entity
public class ParkingLot {

    @Id @GeneratedValue
    @Column(name = "parking_lot_id")
    private Long id;

    private Type type;

    private int capacity;

    private String hours;

    private String fee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id")
    private Location location;


}
