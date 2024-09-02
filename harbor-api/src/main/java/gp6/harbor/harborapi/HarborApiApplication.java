package gp6.harbor.harborapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class HarborApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(HarborApiApplication.class, args);
	}

}
