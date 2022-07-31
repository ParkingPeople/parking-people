package com.apptive.parkingpeople.domain;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
public class TrafficData {

    @Id @GeneratedValue
    @Column(name = "traffic_data_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "location_id", nullable = false)
    private LocationDomain locationDomain;

    private float congestion;

    private Timestamp collected_at;
}
