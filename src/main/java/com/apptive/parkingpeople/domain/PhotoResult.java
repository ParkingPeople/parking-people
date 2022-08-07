package com.apptive.parkingpeople.domain;

import javax.persistence.*;

import lombok.Data;

@Entity
@Data
public class PhotoResult {

    @Id @GeneratedValue
    @Column(name = "photo_result_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) // , optional = false
    @JoinColumn(name = "photo_submission_id") //, nullable = false
    private PhotoSubmission submission;

    private Long model_version;

    private float emptiness;

    @Enumerated(EnumType.STRING)
    private ActivityLevel activityLevel; // erd에는 state인데 구체적으로 바꿈

}
