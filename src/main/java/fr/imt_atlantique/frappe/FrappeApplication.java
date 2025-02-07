package fr.imt_atlantique.frappe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class FrappeApplication {

	public static void main(String[] args) {
		SpringApplication.run(FrappeApplication.class, args);
	}

}
