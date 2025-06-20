package ma.cdgep;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
@SpringBootApplication(scanBasePackages = { "ma.cdgep" })
@EnableAutoConfiguration
@EnableJpaRepositories("ma.cdgep.repository")
public class AidesDirectsApp {

	public static void main(String[] args) {
		SpringApplication.run(AidesDirectsApp.class, args);
	}

}
