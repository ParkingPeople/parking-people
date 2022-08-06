package com.apptive.parkingpeople.service;

import com.apptive.parkingpeople.domain.Location;
import com.apptive.parkingpeople.domain.ParkingLot;
import com.apptive.parkingpeople.domain.PhotoState;
import com.apptive.parkingpeople.vo.Direction;
import com.apptive.parkingpeople.vo.GeometryUtil;
import com.apptive.parkingpeople.vo.LocationPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ParkingLotService {

    private final EntityManager em;

    @Transactional(readOnly = true)
    public List<Location> getParkingLotsWithPoint(double lat, double lon, double range){
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

        Query query = em.createNativeQuery("SELECT name "
                + "FROM location "
                + "WHERE MBRContains(GeomFromText(" + pointFormat + ", coordinates)");
        List<Location> resultList = query.getResultList();

        for(int i = 0; i < resultList.size(); i++) {
            System.out.println(resultList.get(i));
        }

        return resultList;
    }
}
