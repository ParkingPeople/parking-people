package com.apptive.parkingpeople.service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.apptive.parkingpeople.domain.ActivityLevel;
import com.apptive.parkingpeople.domain.Location;
import com.apptive.parkingpeople.domain.ParkingLot;
import com.apptive.parkingpeople.domain.PhotoResult;
import com.apptive.parkingpeople.domain.PhotoSubmission;
import com.apptive.parkingpeople.domain.interfaces.Converter;
import com.apptive.parkingpeople.repository.LocationRepository;
import com.apptive.parkingpeople.repository.ParkingLotRepository;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ParkingLotService {

    // 이거 지워야함
    @Autowired
    LocationRepository locationRepository;

    @Autowired
    ParkingLotRepository parkingLotRepository;

    @PersistenceContext
    private final EntityManager em;

//    @Transactional(readOnly = true)
//    public List<Location> getParkingLotsWithPoint(double lat, double lon, double range){
//        // 범위 안에 있는 주차장 일단 다 가져옴
//        // ran -> km 단위
//        LocationPoint northEast = GeometryUtil
//                .calculate(lat, lon, range, Direction.NORTHEAST.getBearing());
//        LocationPoint southWest = GeometryUtil
//                .calculate(lat, lon, range, Direction.SOUTHWEST.getBearing());
//
//        double ne_y = northEast.getLatitude();
//        double ne_x = northEast.getLongitude();
//        double sw_y = southWest.getLatitude();
//        double sw_x = southWest.getLongitude();
//
//        String pointFormat = String.format("'LINESTRING(%f %f, %f %f, %f %f, %f %f)')", sw_x, sw_y, ne_x, sw_y, ne_x, ne_y, sw_x, ne_y);
//
//        Query query = em.createNativeQuery("SELECT * "
//                + "FROM location as l "
//                + "WHERE MBRContains(GeomFromText(" + pointFormat + ", coordinates)", Location.class);
//
//        List<Location> resultList = query.getResultList();
//
//        System.out.println("범위 안에 있는 주차장 개수 : " + resultList.size());
//
//        // 왜 안되지?....
//        for(Object i : resultList){
//            System.out.println("test1");
//            System.out.println(i);
//        }
//
//        // 사이즈는 나오는데 왜 null이 나오지 // 쿼리문에서 Location 자체를 가져와야 할듯
//        for(int i = 0; i < resultList.size(); i++) {
//            System.out.println("test2");
//            System.out.println(resultList.get(i).getParkingLot().getName());
//        }
//
//        return resultList;
//    }



    public void updateParkingLotsStateByParkingLots(Collection<ParkingLot> parkingLots){
        for(ParkingLot parkingLot : parkingLots){
            setParkingLotState(parkingLot);
        }
        return;
    }

    /**
     * @deprecated Use {@link #updateParkingLotsStateByParkingLots} instead
     * @param locations
     */
    @Deprecated(forRemoval = true)
    public void updateParkingLotsStateByLocations(List<Location> locations){
        return;
    }

    // photoSubmission들로부터 계산, 평군 등을 이용하여 ParkingLot의 state(여유, 보통, 혼잡) 계산하기.
    // 0(FREE), 1(NORMAL), 2(CROWDED)로 가중치 주고, 개수로 나누기
    // 0 ~ 0.5 -> FREE, 0.5 ~ 1.5 -> NORMAL, 1.5 ~ 2.0 -> CROWDED
    // 만약에 하루 안에 사진 정보가 없으면 NONE
    public void setParkingLotState(ParkingLot parkingLot){
        List<PhotoSubmission> photo_submissions = new ArrayList<>(); // 이래야지 nullPointException에 안 걸리는걸로 아는데.. 다시 확인하기.
        photo_submissions = parkingLot.getPhoto_submissions();

        float total = 0;
        float avg = 0;
        int count = 0;

        for(PhotoSubmission submission : photo_submissions){
            LocalDateTime data = submission.getTaken_at();
            LocalDateTime now = LocalDateTime.now();

            if(ChronoUnit.DAYS.between(data, now) < 1){
                // TODO: limit result by time or filter by prefered model id
                for (PhotoResult result: submission.getPhoto_results()) {
                    count++;
                    final ActivityLevel level = result.getActivityLevel();
                    // OR
                    // final ActivityLevel level = getActivityLevelFrom(result);
                    if(level == ActivityLevel.FREE){
                        total += 0.0f;
                    }else if(level == ActivityLevel.NORMAL){
                        total += 1.0f;
                    }else if(level == ActivityLevel.CROWDED){
                        total += 2.0f;
                    }
                }
            }
        }
        if(count == 0) {
            parkingLot.setActivityLevel(ActivityLevel.UNKNOWN);
        }else {

            avg = total / count;
            if (avg < 0.5) {
                parkingLot.setActivityLevel(ActivityLevel.FREE);
            } else if (avg < 1.5) {
                parkingLot.setActivityLevel(ActivityLevel.NORMAL);
            } else {
                parkingLot.setActivityLevel(ActivityLevel.CROWDED);
            }
        }
        parkingLotRepository.save(parkingLot); // FIXME: 이걸 해줘야 하나? 이거 안해도 저절로 되는걸로 아는데?.. 왜 이러지?...
        return;
    }

    private ActivityLevel getActivityLevelFrom(PhotoResult result) {
        return ActivityLevelConverter.instance.convert(result.getEmptiness());
    }

    static final class ActivityLevelConverter implements Converter<Float, ActivityLevel> {

        private ActivityLevelConverter() {}

        public static ActivityLevelConverter instance = new ActivityLevelConverter();

        @Override
        public @NonNull ActivityLevel convert(@NonNull Float emptiness) {
            // TODO: implement this
            throw new UnsupportedOperationException("Not implemented yet");
        }

    }
}
