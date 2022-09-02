package com.apptive.parkingpeople.service;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

import com.apptive.parkingpeople.dto.RecommendResponseDto;
import com.apptive.parkingpeople.dto.StatusEnum;
import com.mysema.commons.lang.Pair;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
        parkingLotRepository.save(parkingLot);
    }

    public ResponseEntity<RecommendResponseDto> prioritizeParkingLotUsingActivityLevelAndWalkingTime(List<ParkingLot> parkingLots){
        Map<ParkingLot, Long> parkingLotsInMap = new HashMap<>();
        for(ParkingLot p : parkingLots){
            parkingLotsInMap.put(p, p.getTimeToDes());
        }


        Vector<Pair<ParkingLot, Long>> weight = new Vector<>();
        for(Map.Entry<ParkingLot, Long> elem : parkingLotsInMap.entrySet()){
            if(elem.getKey().getActivityLevel().equals(ActivityLevel.FREE)){
                weight.add(new Pair<>(elem.getKey(), elem.getValue() + 0L));
            }else if(elem.getKey().getActivityLevel().equals(ActivityLevel.NORMAL)){
                weight.add(new Pair<>(elem.getKey(), elem.getValue() + 300L));
            }else if(elem.getKey().getActivityLevel().equals(ActivityLevel.CROWDED)){
                weight.add(new Pair<>(elem.getKey(), elem.getValue() + 600L));
            }else if(elem.getKey().getActivityLevel().equals(ActivityLevel.UNKNOWN)){
                weight.add(new Pair<>(elem.getKey(), elem.getValue() + 300L)); // 일단은 NORMAL로 계산
            }
        }

        weight.sort(Comparator.comparing(Pair::getSecond));

        List<ParkingLot> bestParkingLots = new ArrayList<>();
        for(Pair<ParkingLot, Long> it : weight){
            bestParkingLots.add(it.getFirst());
        }

        // 일단 5개 이하로 던져주기
        if(bestParkingLots.size() > 5)
            bestParkingLots = bestParkingLots.subList(0, 5);

        RecommendResponseDto recommendResponseDto = new RecommendResponseDto();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));

        recommendResponseDto.setStatus(StatusEnum.OK);
        recommendResponseDto.setTotal_count(bestParkingLots.size());
        recommendResponseDto.setParkingLots(bestParkingLots);

        return new ResponseEntity<>(recommendResponseDto, headers, HttpStatus.OK);
    }

    public List<ParkingLot> findParkingLotsByLocations(List<Location> locations){
        List<ParkingLot> parkingLots = new ArrayList<>();
        for(Location l : locations){
            parkingLots.add(parkingLotRepository.findById(l.getId()).get());
        }
        return parkingLots;
    }
}
