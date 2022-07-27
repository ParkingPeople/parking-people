package com.apptive.parkingpeople.domain;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
public class TrafficData {

    @Id @GeneratedValue
    @Column(name = "traffic_data_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id")
    private Location location;

    private float congestion;

    private Timestamp collected_at;
}
