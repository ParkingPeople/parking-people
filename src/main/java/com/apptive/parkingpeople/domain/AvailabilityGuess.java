package com.apptive.parkingpeople.domain;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
public class AvailabilityGuess {

    @Id @GeneratedValue
    @Column(name = "availability_guess_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parking_lot_id")
    private ParkingLot lot;

    private int model_version;

    private float availability;

    private Timestamp computed_at;
}
