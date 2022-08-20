package com.apptive.parkingpeople.domain;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;

import org.locationtech.jts.geom.Point;

import lombok.AllArgsConstructor;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;

@Entity
@Inheritance
@DiscriminatorColumn(name = "location_type", discriminatorType = DiscriminatorType.INTEGER) //TODO
public class Location {

    // 스프링에서 generatedvalue가 되는거지, import할때 올라가는거는 아님. // wnc
    @Id
    @GeneratedValue
    @Column(name = "location_id")
    private Long id;

    private String name;

    private String address;

    private double longitude;

    private double latitude;
    private Point coordinates;


    private String address_old;

    // 양방향
    // @OneToOne(mappedBy = "location")
    // private ParkingLot parkingLot;
    //
    // @OneToOne(mappedBy = "location")
    // private TrafficData trafficData;

    static private final String _PARKING_LOT_DV = "0";
    static private final String _TRAFFIC_DATA_DV = "1";

    // TODO
    @AllArgsConstructor
    public static enum Type {
        PARKING_LOT(ParkingLot.class, Location._PARKING_LOT_DV),
        TRAFFIC_DATA(TrafficData.class, Location._TRAFFIC_DATA_DV);

        public final Class<?> entityClass;
        public final String discriminatorValue;

        static final String PARKING_LOT_DV = Location._PARKING_LOT_DV;
        static final String TRAFFIC_DATA_DV = Location._TRAFFIC_DATA_DV;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    // 주차장 Data import할때 Point라는 객체가 없어서 이렇게 해줘야 함.
    public Point getCoordinates() throws ParseException {
        if(coordinates.isEmpty()){
            // Location
            String pointWKT = String.format("POINT(%s %s)", longitude, latitude);
            // WKTReader를 통한 WKT를 실제 타입으로 변환
            Point point = (Point) new WKTReader().read(pointWKT);
            coordinates = point;
        }
        return coordinates;
    }
    public void setCoordinates(Point coordinates) {
        this.coordinates = coordinates;
    }

    public String getAddress_old() {
        return address_old;
    }

    public void setAddress_old(String address_old) {
        this.address_old = address_old;
    }


}
