package com.apptive.parkingpeople.service;

import com.apptive.parkingpeople.domain.Location;
import com.apptive.parkingpeople.service.util.GeometryUtil;
import com.apptive.parkingpeople.service.util.TypeUtil;
import com.apptive.parkingpeople.vo.Direction;
import com.apptive.parkingpeople.vo.LocationPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LocationService {

    @PersistenceContext
    private final EntityManager em;

    @Transactional(readOnly = true)
    public List<Location> getLocationsWithinPoint(double lat, double lon, double range){
        // 범위 안에 있는 주차장 일단 다 가져옴
        // ran -> km 단위
        LocationPoint northEast = GeometryUtil
                .calculate(lat, lon, range, Direction.NORTHEAST.getBearing());
        LocationPoint southWest = GeometryUtil
                .calculate(lat, lon, range, Direction.SOUTHWEST.getBearing());

        double ne_y = northEast.getLatitude();
        double ne_x = northEast.getLongitude();
        double sw_y = southWest.getLatitude();
        double sw_x = southWest.getLongitude();

        String pointFormat = String.format("'LINESTRING(%f %f, %f %f, %f %f, %f %f)')", sw_x, sw_y, ne_x, sw_y, ne_x, ne_y, sw_x, ne_y);

        Query query = em.createNativeQuery("SELECT * "
                + "FROM location as l "
                + "WHERE MBRContains(GeomFromText(" + pointFormat + ", coordinates)", Location.class);

        List<Location> resultList = TypeUtil.castList(Location.class, query.getResultList());

        return resultList;
    }
}
