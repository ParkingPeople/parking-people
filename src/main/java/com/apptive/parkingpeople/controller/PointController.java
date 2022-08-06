package com.apptive.parkingpeople.controller;

import com.apptive.parkingpeople.service.PointService;
import org.locationtech.jts.io.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/point")
public class PointController {

    @Autowired
    PointService pointService;

    @GetMapping("save")
    public String saveLocation(@RequestParam("name") String name, @RequestParam("lon") double lon, @RequestParam("lat") double lat) throws ParseException {
        return pointService.save(name, lon, lat);
    }

//    http://localhost:8080/point/near?lon=129.086301&lat=35.220105&range=2
    @GetMapping("near")
    public int getNearByLocationDomains(@RequestParam("lon") double lon, @RequestParam("lat") double lat, @RequestParam("range") double range){
        return pointService.getNearByLocationDomains(lat, lon, range);
    }

    // 범위 안에 있는 주차장 다 들고 오고
    // '여유' 중에 목적지와 거리 가까운거 // '보통' 중에 목적지와 거리 가까운거 // '혼잡'중에 목적지와 거리 가까운거 // 순서로 정렬
    // 아니면 5개 쌓이면 바로 return 해도 되고
}
