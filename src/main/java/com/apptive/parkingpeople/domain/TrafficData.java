package com.apptive.parkingpeople.domain;

import javax.persistence.*;

import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
public class TrafficData {

    @Id @GeneratedValue
    @Column(name = "traffic_data_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "location_id", nullable = false)
    private Location location;

    private float congestion;

    private LocalDateTime collected_at;
}
