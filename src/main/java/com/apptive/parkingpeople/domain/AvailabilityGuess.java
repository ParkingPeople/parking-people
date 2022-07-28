package com.apptive.parkingpeople.domain;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
public class AvailabilityGuess {

    @Id @GeneratedValue
    @Column(name = "availability_guess_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "parking_lot_id", nullable = false)
    private ParkingLot lot;

    private Long model_version;

    private float availability;

    private Timestamp computed_at;

}
