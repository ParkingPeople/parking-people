package com.apptive.parkingpeople.domain;

import java.util.List;

import javax.persistence.*;

@Entity
public class ParkingLot {

    @Id @GeneratedValue
    @Column(name = "parking_lot_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private LocationType type;

    private Long capacity;

    private String hours;

    private String fee;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "location_id", nullable = false)
    private LocationDomain locationDomain;

    @OneToMany(mappedBy = "lot")
    private List<AvailabilityGuess> availability_guesses;

    @OneToMany(mappedBy = "lot")
    List<PhotoSubmission> photos;

}
