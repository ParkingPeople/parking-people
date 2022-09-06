package com.apptive.parkingpeople.service;

import com.apptive.parkingpeople.domain.ActivityLevel;
import com.apptive.parkingpeople.domain.Location;
import com.apptive.parkingpeople.domain.ParkingLot;
import com.apptive.parkingpeople.domain.PhotoSubmission;
import com.apptive.parkingpeople.dto.MLDto;
import com.apptive.parkingpeople.repository.ParkingLotRepository;
import com.apptive.parkingpeople.repository.PhotoSubmissionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MLService {

    private final ParkingLotRepository parkingLotRepository;

    private final PhotoSubmissionRepository photoSubmissionRepository;

    public ResponseEntity<Void> setActivityLevel(List<Location> locations, MultipartFile file) {

        double emptyProbability;

        try {

            ByteArrayResource fileResource = new ByteArrayResource(file.getBytes()) {
                // 기존 ByteArrayResource의 getFilename 메서드 override
                @Override
                public String getFilename() {
                    return "emptyProbability";
                }
            };

            MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
            params.add("files", fileResource);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<>(params, headers);

            String apiURL = "http://ec2-43-201-27-101.ap-northeast-2.compute.amazonaws.com:8000/upload";

            RestTemplate restTemplate = new RestTemplate();

            ResponseEntity<MLDto> response = restTemplate.exchange(apiURL, HttpMethod.POST, entity, MLDto.class);

            emptyProbability = response.getBody().getEmptyProbability();
        }catch (Exception e){
            System.out.println("=== Exception ===");
            System.out.println(e);
            log.error("ML 서버 이상 감지");
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        ActivityLevel activityLevel;

        double standard = 0.1;
        if(emptyProbability >= standard){
            activityLevel = ActivityLevel.FREE;
        }
        else{
            activityLevel = ActivityLevel.CROWDED;
        }

        LocalDateTime time = LocalDateTime.now();

        for(Location location : locations) {
            Long id = location.getId();
            Optional<ParkingLot> parkingLot = parkingLotRepository.findById(id);

            PhotoSubmission photoSubmission = new PhotoSubmission();

            photoSubmission.setLot(parkingLot.get());
            photoSubmission.setTaken_at(time);
            photoSubmission.setPhotoResult(activityLevel);

            photoSubmissionRepository.save(photoSubmission);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
