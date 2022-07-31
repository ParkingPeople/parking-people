package com.apptive.parkingpeople.domain;

import javax.persistence.*;

import lombok.Getter;
import lombok.Setter;
import org.locationtech.jts.geom.Point;

//TODO Point 사용시 Loation이라는 라이브러리 있어서 일단은 LocationDomain으로 바꿔놨음.
@Entity
@Getter
@Setter
public class LocationDomain {

    @Id @GeneratedValue
    @Column(name = "location_domain_id")
    //@OneToOne(mappedBy = "location", fetch = FetchType.LAZY) //TODO @OneToOne일 경우, Long 또는 int를 못 받아서 다른걸로 해야 할거 같아요
    private Long id;

    @Enumerated(EnumType.STRING)
    private LocationType type;

    private String name;

    private String address;

    private Point coordinates;

    private String address_old;

}
