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

    private int earned;

    private PointReason reason;

    private String reference;

}
