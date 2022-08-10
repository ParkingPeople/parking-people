package com.apptive.parkingpeople.domain;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import lombok.Data;

@Data
@Entity
public class PhotoSubmission {

    @Id @GeneratedValue
    @Column(name = "photo_submission_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) //일단 optional = false 뺴놓음
    @JoinColumn(name = "user_id") // 일단 nullable = false 빼놓음
    private User submitter;

    @ManyToOne(fetch = FetchType.LAZY) //, optional = false
    @JoinColumn(name = "parking_lot_id") // , nullable = false
    private ParkingLot lot;

    private LocalDateTime taken_at;

    @Enumerated(EnumType.STRING)
    private TaskStatus submissionState; // erd에는 state인데 구체적으로 바꿈

    // TODO: restore jpa relation
    // 양방향
    @OneToMany(mappedBy = "submission")
    private List<PhotoResult> photo_results;

    public PhotoResult getPreferredResult() {
        // TODO: get default preferred result or throw if none were fit
        throw new UnsupportedOperationException("Not implemented yet");
    }

}
