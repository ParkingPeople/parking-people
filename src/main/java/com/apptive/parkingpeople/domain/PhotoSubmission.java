package com.apptive.parkingpeople.domain;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Entity
public class PhotoSubmission {

    @Id @GeneratedValue
    @Column(name = "photo_submission_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User submitter;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "parking_lot_id", nullable = false)
    private ParkingLot lot;

    private Timestamp taken_at;

    private AnalysisState state;

    @OneToMany(mappedBy = "submission")
    private List<PhotoResult> photo_results;

}
