package tn.esprit.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication
@EnableScheduling
public class GestionStationSkiApplication {

	public static void main(String[] args) {
		SpringApplication.run(GestionStationSkiApplication.class, args);
	}

}
