package com.apptive.parkingpeople.domain;

import lombok.Getter;
import lombok.Setter;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class ParkingLot {

    @Id @GeneratedValue
    @Column(name = "parking_lot_id")
    private Long id;

    // 주차장 이름으로 등록을 해야 할거 같은데, 일단 erd에는 없음. 이거 확인하기.
    @Column(unique = true)
    private String name;

    // 주차장 유형
    @Enumerated(EnumType.STRING)
    private ParkingLotType parkingLotType; // erd에서는 type으로 표기되어 있음

    // 주차장 구분
    private boolean is_public;

    // 주차장 구획수
    private Long capacity;

    // 급지 구분
    private Long parkingClass; // erd에서는 class // 주차장 급지구분 // int써도 되는데, 숫자형식은 그냥 다 long으로 씀.

    // 평일 운영 시작 시간
    private LocalTime opens_at_workdays;

    // 평일 운영 종료 시간
    private LocalTime closes_at_workdays;

    // 토요일 운영 시작 시간
    private LocalTime opens_at_saturdays;

    // 토요일 운영 종료 시간
    private LocalTime closes_at_saturdays;

    // 공휴일 운영 시작 시간
    private LocalTime opens_at_holidays;

    // 공휴일 운영 종룔 시간
    private LocalTime closes_at_holidays;

    // 위도 경도를 Location의 Point에 넣어야 함
    @OneToOne(fetch = FetchType.LAZY, optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "location_id", nullable = false)
    private Location location;

    // 주차장 번호
    private String external_id;

    // 주차 기본 요금
    private Long base_fee;

    // 주차 기본 시간
    private Duration base_fee_duration;

    // 추가 단위 요금
    private Long extra_fee;

    // 추가 단위 시간
    private Duration extra_fee_duration;

    // 1일 주차권 요금 적용 시간
    private Long daily_fee;

    // 월 정기권 요금
    private Long monthly_fee;

    // 전화번호
    private String contact; // 이건 머지?

    // 전화 번호
    private LocalDate updated_at;

    // 양방향
//    @OneToMany(mappedBy = "lot")
//    private List<AvailabilityGuess> availability_guesses;
//
//    @OneToMany(mappedBy = "lot")
//    private List<PhotoSubmission> photo_submissions;

}
