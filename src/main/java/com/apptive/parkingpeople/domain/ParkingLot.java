package com.apptive.parkingpeople.domain;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;

import javax.persistence.*;

@Entity
public class ParkingLot {

    @Id @GeneratedValue
    @Column(name = "parking_lot_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private ParkingLotType parkingLotType; // erd에서는 type으로 표기되어 있음

    private boolean is_public;

    private Long capacity;

    private Long parkingClass; // erd에서는 class // 주차장 급지구분 // int써도 되는데, 숫자형식은 그냥 다 long으로 씀.

    private LocalTime opens_at_workdays;

    private LocalTime closes_at_workdays;

    private LocalTime opens_at_saturdays;

    private LocalTime closes_at_saturdays;

    private LocalTime opens_at_holidays;

    private LocalTime closes_at_holidays;

    @OneToOne(fetch = FetchType.LAZY, optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "parking_lot_location_id", nullable = false)
    private ParkingLotLocation parkingLotLocation;

    // UK를 사용해야 하는 이유?
    private String external_id;

    private Long base_fee;

    private Duration base_fee_duration;

    private Long extra_fee;

    private Duration extra_fee_duration;

    private Long daily_fee;

    private Long monthly_fee;

    private String contact; // 이건 머지?

    private LocalDate updated_at;

    // 양방향
//    @OneToMany(mappedBy = "lot")
//    private List<AvailabilityGuess> availability_guesses;
}
