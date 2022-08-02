package com.apptive.parkingpeople.domain;

import javax.persistence.*;

@Entity
public class PointHistory {

    @Id @GeneratedValue
    @Column(name = "point_history_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private Long earned;

    @Enumerated(EnumType.STRING)
    private PointReason pointReason; // erd에는 reason인데 구체적으로 바꿈

    private String reference;

}
