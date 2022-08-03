package com.apptive.parkingpeople;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class ParkingPeopleApplication {

	public static void main(String[] args) {
		SpringApplication.run(ParkingPeopleApplication.class, args);
	}

}
