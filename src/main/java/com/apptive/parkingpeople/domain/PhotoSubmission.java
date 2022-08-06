package com.apptive.parkingpeople.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Getter @Setter
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

    private Timestamp taken_at;

    @Enumerated(EnumType.STRING)
    private PhotoState photoState; // erd에는 state인데 구체적으로 바꿈

    // 양방향
//    @OneToMany(mappedBy = "submission")
//    private List<PhotoResult> photo_results;

}
