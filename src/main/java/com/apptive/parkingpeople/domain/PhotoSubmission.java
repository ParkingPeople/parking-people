package com.apptive.parkingpeople.domain;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonBackReference;
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

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY) //, optional = false
    @JoinColumn(name = "parking_lot_id") // , nullable = false
    private ParkingLot lot;

    private LocalDateTime taken_at;

    @Enumerated(EnumType.STRING)
    private TaskStatus submissionState; // erd에는 state인데 구체적으로 바꿈

    @Enumerated(EnumType.STRING)
    private ActivityLevel photoResult;

    private Long model_version;

    // TODO photoResult로 대신했는데, 혹시나 몰라서 놔뒀습니다. 확인하고 지워주세요
//    public PhotoResult getPreferredResult() {
//        // TODO: get default preferred result or throw if none were fit
//        throw new UnsupportedOperationException("Not implemented yet");
//    }

}
