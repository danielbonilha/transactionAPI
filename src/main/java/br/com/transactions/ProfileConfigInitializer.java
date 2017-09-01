package br.com.transactions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.embedded.AnnotationConfigEmbeddedWebApplicationContext;
import org.springframework.cloud.Cloud;
import org.springframework.cloud.CloudFactory;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.core.env.ConfigurableEnvironment;

import br.com.transactions.constants.Constants;


public class ProfileConfigInitializer implements ApplicationContextInitializer<AnnotationConfigEmbeddedWebApplicationContext> {
	
	Logger logger = LoggerFactory.getLogger(ProfileConfigInitializer.class);

	@Override
	public void initialize(AnnotationConfigEmbeddedWebApplicationContext appContext) {
		Cloud cloud = getCloud();
		ConfigurableEnvironment appEnv = appContext.getEnvironment();
		
		if (cloud != null) {
			logger.info("*** Runtime Environment: Cloud ***");
			appEnv.addActiveProfile(Constants.PROFILE_CLOUD);
		} else {
			logger.info("*** Runtime Environment: Local ***");
			appEnv.addActiveProfile(Constants.PROFILE_LOCAL);
		}
	}
	
	private Cloud getCloud() {
		try {
			CloudFactory factory = new CloudFactory();
			return factory.getCloud();
		} catch (Exception e) {
			return null;
		}
	}

}
