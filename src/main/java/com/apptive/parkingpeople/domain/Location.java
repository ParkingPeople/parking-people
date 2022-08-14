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
import lombok.Data;

@Data
@Entity
@Inheritance
@DiscriminatorColumn(name = "location_type", discriminatorType = DiscriminatorType.INTEGER)
public class Location {

    @Id
    @GeneratedValue
    @Column(name = "location_id")
    private Long id;

    private String name;

    private String address;

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

    @AllArgsConstructor
    public static enum Type {
        PARKING_LOT(ParkingLot.class, Location._PARKING_LOT_DV),
        TRAFFIC_DATA(TrafficData.class, Location._TRAFFIC_DATA_DV);

        public final Class<?> entityClass;
        public final String discriminatorValue;

        static final String PARKING_LOT_DV = Location._PARKING_LOT_DV;
        static final String TRAFFIC_DATA_DV = Location._TRAFFIC_DATA_DV;
    }

}
