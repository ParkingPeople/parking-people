package com.apptive.parkingpeople.service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.stereotype.Service;

import com.apptive.parkingpeople.domain.ActivityLevel;
import com.apptive.parkingpeople.domain.Location;
import com.apptive.parkingpeople.domain.ParkingLot;
import com.apptive.parkingpeople.domain.PhotoSubmission;
import com.apptive.parkingpeople.repository.ParkingLotRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ParkingLotService {

    private final ParkingLotRepository parkingLotRepository;


    public void updateParkingLotsStateByParkingLots(Collection<ParkingLot> parkingLots){
        for(ParkingLot parkingLot : parkingLots){
            setParkingLotState(parkingLot);
        }
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

        List<PhotoSubmission> photo_submissions = parkingLot.getPhoto_submissions();

        float total = 0;
        float avg;
        int count = 0;

        for(PhotoSubmission submission : photo_submissions){
            LocalDateTime data = submission.getTaken_at();
            LocalDateTime now = LocalDateTime.now();

            if(ChronoUnit.DAYS.between(data, now) < 1){
                // TODO: limit result by time or filter by prefered model id
                count++;
                if(submission.getPhotoResult() == ActivityLevel.FREE)
                    total += 0.0f;
                else if(submission.getPhotoResult() == ActivityLevel.NORMAL)
                    total += 1.0f;
                else if(submission.getPhotoResult() == ActivityLevel.CROWDED)
                    total += 2.0f;
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
    }

    // TODO photoSubmission 도메인에서 photoResult로 대신했는데, 혹시나 몰라서 놔뒀습니다. 확인하고 지워주세요
//    private ActivityLevel getActivityLevelFrom(PhotoResult result) {
//        return ActivityLevelConverter.instance.convert(result.getEmptiness());
//    }

//    static final class ActivityLevelConverter implements Converter<Float, ActivityLevel> {
//
//        private ActivityLevelConverter() {}
//
//        public static ActivityLevelConverter instance = new ActivityLevelConverter();
//
//        @Override
//        public @NonNull ActivityLevel convert(@NonNull Float emptiness) {
//            // TODO: implement this
//            throw new UnsupportedOperationException("Not implemented yet");
//        }
//
//    }
}
