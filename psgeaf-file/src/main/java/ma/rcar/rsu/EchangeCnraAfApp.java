package ma.rcar.rsu;



import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;



@SpringBootApplication(scanBasePackages = { "ma.rcar.rsu" })

@EnableAsync
public class EchangeCnraAfApp {


	public static void main(String[] args) {
		SpringApplication.run(EchangeCnraAfApp.class, args);
	}


}
