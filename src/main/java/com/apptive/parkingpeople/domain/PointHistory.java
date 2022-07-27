package com.apptive.parkingpeople.domain;

import javax.persistence.*;

@Entity
public class PointHistory {

    @Id @GeneratedValue
    @Column(name = "point_history_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private int earned;

    private Reason reason;

    private String reference;
}
