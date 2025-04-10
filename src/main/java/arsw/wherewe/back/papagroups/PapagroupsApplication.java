package arsw.wherewe.back.papagroups;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class PapagroupsApplication {

	public static void main(String[] args) {
		SpringApplication.run(PapagroupsApplication.class, args);
	}

}
