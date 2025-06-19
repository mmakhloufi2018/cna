package ma.cdgep;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;

//@Component
public class Relance implements CommandLineRunner {

	@Autowired
	ServiceRunner serviceRunner;

	@Override
	public void run(String... args) throws Exception {

		System.out.println("-------------------DEBUT--------------------");
//		List<CompletableFuture<Void>> futures = new ArrayList<CompletableFuture<Void>>();
//
//		for (int i = 0; i < 500; i++) {
//
//			CompletableFuture<Void> future = serviceRunner.run(i);
//
//			futures.add(future);
//
//		}
//
//		CompletableFuture<?>[] futuresArray = futures.stream().toArray(CompletableFuture[]::new);
//
//		CompletableFuture.allOf(futuresArray).join();

		System.out.println("end");

		System.out.println("-----------------FIN---------------------");
	}

}
