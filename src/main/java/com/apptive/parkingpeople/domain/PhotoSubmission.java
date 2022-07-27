package com.apptive.parkingpeople.domain;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
public class PhotoSubmission {

    @Id @GeneratedValue
    @Column(name = "photo_submission_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User submitter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parking_lot_id")
    private ParkingLot lot;

    private Timestamp taken_at;

    private State state;






}
