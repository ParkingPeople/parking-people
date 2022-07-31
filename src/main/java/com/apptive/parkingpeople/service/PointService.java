package com.apptive.parkingpeople.service;

import com.apptive.parkingpeople.domain.LocationDomain;
import com.apptive.parkingpeople.repository.LocationRepository;
import com.apptive.parkingpeople.vo.Direction;
import com.apptive.parkingpeople.vo.GeometryUtil;
import com.apptive.parkingpeople.vo.Location;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PointService {

    @Autowired
    LocationRepository locationRepository;

    private final EntityManager em;

    public String save(String name, double lon, double lat) throws ParseException {
        String pointWKT = String.format("POINT(%s %s)", lon, lat);

        // WKTReader를 통한 WKT를 실제 타입으로 변환
        Point point = (Point) new WKTReader().read(pointWKT);
        LocationDomain location = new LocationDomain();
        location.setName(name);
        location.setCoordinates(point);

        locationRepository.save(location);

        return "good";
    }

    @Transactional(readOnly = true)
    public int getNearByLocationDomains(double lat, double lon, double range){
        //ran -> km 단위
        Location northEast = GeometryUtil
                .calculate(lat, lon, range, Direction.NORTHEAST.getBearing());
        Location southWest = GeometryUtil
                .calculate(lat, lon, range, Direction.SOUTHWEST.getBearing());

        double x1 = northEast.getLatitude();
        double y1 = northEast.getLongitude();
        double x2 = southWest.getLatitude();
        double y2 = southWest.getLongitude();

        String pointFormat = String.format("'LINESTRING(%f %f, %f %f)')", x1, y1, x2, y2);

        // 쿼리문 확인 필요
        Query query = em.createNativeQuery("SELECT r.name "
                        + "FROM `parking-people`.location_domain AS r "
                        + "WHERE MBRContains(ST_LINESTRINGFROMTEXT(" + pointFormat + ", r.coordinates)", LocationDomain.class)
                .setMaxResults(10);

        List<LocationDomain> locationDomains = query.getResultList();
        for(int i = 0; i < locationDomains.size(); i++){
            System.out.println("test" + i);
            System.out.println(locationDomains.get(i).getName());
        }
        int size;
        if(!locationDomains.isEmpty()) // null 고려되는지 확인
            return locationDomains.size();
        else
            return 0;


    }

}
