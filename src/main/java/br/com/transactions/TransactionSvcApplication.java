package br.com.transactions;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class TransactionSvcApplication {

	public static void main(String[] args) {
		new SpringApplicationBuilder(TransactionSvcApplication.class)
	    	.initializers(new ProfileConfigInitializer())
	    	.run(args);
	}
}
