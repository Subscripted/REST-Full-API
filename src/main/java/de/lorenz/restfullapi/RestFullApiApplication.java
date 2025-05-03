package de.lorenz.restfullapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class RestFullApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(RestFullApiApplication.class, args);
    }

}
