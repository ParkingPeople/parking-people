package com.apptive.parkingpeople.service;

import com.apptive.parkingpeople.domain.ParkingLotLocation;
import com.apptive.parkingpeople.repository.ParkingLotLocationRepository;
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
    ParkingLotLocationRepository parkingLotLocationRepository;

    private final EntityManager em;

    public String save(String name, double lon, double lat) throws ParseException {
        String pointWKT = String.format("POINT(%s %s)", lon, lat);

        // WKTReader를 통한 WKT를 실제 타입으로 변환
        Point point = (Point) new WKTReader().read(pointWKT);
        ParkingLotLocation location = new ParkingLotLocation();
        location.setName(name);
        location.setCoordinates(point);

        parkingLotLocationRepository.save(location);

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

//        String pointFormat = String.format("'LINESTRING(%f %f, %f %f)')", x1, y1, x2, y2); // 점 2개로도 할 수 있는 듯
        String pointFormat = String.format("'LINESTRING(%f %f, %f %f, %f %f, %f %f)')", y2, x2, y1, x2, y1, x1, y2, x1);

        Query query = em.createNativeQuery("SELECT name "
                + "FROM location_domain "
                + "WHERE MBRContains(GeomFromText(" + pointFormat + ", coordinates)");

        List resultList = query.getResultList();
        int size = resultList.size();
        System.out.println("size : " + size);
        System.out.println("기준 위도 : " + lat + ", 기준 경도 : " + lon + ", 범위(km) : " + range);
        System.out.println("[범위 안에 속하는 지역]");
        for(int i = 0; i < resultList.size(); i++) {
            System.out.println(resultList.get(i));
        }

        return size;

    }

}
