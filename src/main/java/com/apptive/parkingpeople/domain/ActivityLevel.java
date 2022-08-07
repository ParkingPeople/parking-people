package com.apptive.parkingpeople.domain;

// 여유, 보통, 혼잡
// PakringLot에서 하루 안에 있는 주차 정보가 없는 경우 NONE 처리
public enum ActivityLevel {
    FREE, NORMAL, CROWDED, UNKNOWN
}
