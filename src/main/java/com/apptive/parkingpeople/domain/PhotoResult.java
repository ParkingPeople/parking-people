package com.apptive.parkingpeople.domain;

import javax.persistence.*;

@Entity
public class PhotoResult {

    @Id @GeneratedValue
    @Column(name = "photo_result_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "photo_submission_id")
    private PhotoSubmission submission;

    private int model_version;

    private float emptiness;
}
