package com.apptive.parkingpeople.service;

import com.apptive.parkingpeople.vo.Direction;
import com.apptive.parkingpeople.vo.GeometryUtil;
import com.apptive.parkingpeople.vo.LocationPoint;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TrafficCongestionService {

    @Autowired
    TrafficDataService trafficDataService;

    @Value("${tmap-key}")
    private String tmapKey;

    public Point getBestPointByComparing(double des_lat, double des_lon, int range_meter) throws JsonProcessingException, ParseException {
        // 사각형을 4개 그렸을때 그 중점들 까지의 거리이므로 range_meter를 반 잘라줘야 함
        range_meter = range_meter / 2;
        // calculate에서는 km단위를 사용하므로 range_meter * 0.001d 해야함
        LocationPoint northWest = GeometryUtil
                .calculate(des_lat, des_lon, range_meter * 0.001d, Direction.NORTHWEST.getBearing());
        LocationPoint northEast = GeometryUtil
                .calculate(des_lat, des_lon, range_meter * 0.001d, Direction.NORTHEAST.getBearing());
        LocationPoint southWest = GeometryUtil
                .calculate(des_lat, des_lon, range_meter * 0.001d, Direction.SOUTHWEST.getBearing());
        LocationPoint southEast = GeometryUtil
                .calculate(des_lat, des_lon, range_meter * 0.001d, Direction.SOUTHEAST.getBearing());

        double north_west_x = northWest.getLatitude();
        double north_west_y = northWest.getLongitude();
        double north_east_x = northEast.getLatitude();
        double north_east_y = northEast.getLongitude();
        double south_west_x = southWest.getLatitude();
        double south_west_y = southWest.getLongitude();
        double south_east_x = southEast.getLatitude();
        double south_east_y = southEast.getLongitude();

        String pointWKT = String.format("POINT(%s %s)", north_west_y, north_west_x);
        Point point_nw = (Point) new WKTReader().read(pointWKT);

        pointWKT = String.format("POINT(%s %s)", north_east_y, north_east_x);
        Point point_ne = (Point) new WKTReader().read(pointWKT);

        pointWKT = String.format("POINT(%s %s)", south_west_y, south_west_x);
        Point point_sw = (Point) new WKTReader().read(pointWKT);

        pointWKT = String.format("POINT(%s %s)", south_east_y, south_east_x);
        Point point_se = (Point) new WKTReader().read(pointWKT);


        HashMap<Point, Double> map = new HashMap<Point, Double>();
        map.put(point_nw, trafficDataService.getCongestion(north_west_x, north_west_y));
        map.put(point_ne, trafficDataService.getCongestion(north_east_x, north_east_y));
        map.put(point_sw, trafficDataService.getCongestion(south_west_x, south_west_y));
        map.put(point_se, trafficDataService.getCongestion(south_east_x, south_east_y));
        System.out.println(map.get(point_nw));
        System.out.println(map.get(point_ne));
        System.out.println(map.get(point_sw));
        System.out.println(map.get(point_se));


        List<Map.Entry<Point, Double>> entryList = new LinkedList<>(map.entrySet());
        entryList.sort(Map.Entry.comparingByValue());
        System.out.println(entryList.get(0));

        return entryList.get(0).getKey();
    }
}
