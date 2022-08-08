package com.apptive.parkingpeople;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
//@SpringBootApplication(exclude = WebSecurityConfig.class)
@SpringBootApplication
public class ParkingPeopleApplication {

	public static void main(String[] args) {
		SpringApplication.run(ParkingPeopleApplication.class, args);
	}

}
