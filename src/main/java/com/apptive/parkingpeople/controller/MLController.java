package com.apptive.parkingpeople.controller;

import com.apptive.parkingpeople.service.MLService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class MLController {

    private final MLService mlService;


    @GetMapping("/upload/test/1")
    public String test1(){
        return "1";
    }

    @PostMapping("/upload/test/2")
    public String test2(){
        return "2";
    }

    @PostMapping("/upload/file") // test for git action
    public Boolean uploadFile(@RequestParam("id") Long id, @RequestParam("file") MultipartFile file){

        double emptyProbability = mlService.setActivityLevel(id, file);

        if(emptyProbability == 500)
            return false;
        else
            return true;
    }
}
