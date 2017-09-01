package br.com.transactions;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class ApogeoSolutionSvcApplication {

	public static void main(String[] args) {
		new SpringApplicationBuilder(ApogeoSolutionSvcApplication.class)
	    	.initializers(new ProfileConfigInitializer())
	    	.run(args);
	}
}
