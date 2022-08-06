package com.apptive.parkingpeople;

import com.apptive.parkingpeople.config.security.WebSecurityConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

//@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
//@SpringBootApplication(exclude = WebSecurityConfig.class)
@SpringBootApplication
public class ParkingPeopleApplication {

	public static void main(String[] args) {
		SpringApplication.run(ParkingPeopleApplication.class, args);
	}

}
